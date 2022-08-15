
function createReplyCommentHtmlCode(parent_id, comment_id){
    let Html = "";
    Html += "<img class='comment-block-reply-front-image' id='comment-block-reply-front-image-reply' src= '/resources/png/right-and-down.png' /img>";
    Html += "<div class='comment-block-reply' id='comment-write-block-reply'>";
    Html += "<h4 class = 'comment-headline-text-writer'> 답글 추가 하기 </h4>";
    Html += "<textarea class='comment-write-content' placeholder='댓글 내용을 작성해주세요' id='comment-content-reply'></textarea>"
    Html += "<div class='comment-write-bottom'>"
        + "<div class='comment-write-info'>"
        + "<input type='text' class='comment-write-name' placeholder='아이디' id='comment-writer-name-reply'> </input>"
        + "<input type='password' class='comment-write-password' placeholder='비밀번호' id='comment-writer-password-reply'>  </input>"
        + "</div>"
        + "<button class='comment-write-submit-button' id='comment-submit-reply-cancel'> 취소 </button>"
    Html +=  "<button class='comment-write-submit-button' id='comment-submit-reply' onclick='addComment(" + parent_id + ',' + comment_id + ")'> 답글작성 </button>";
    Html += "</div>" + "</div>" + "</div>";
    return Html;
}

function createUpdateCommentHtmlCode(parent_id, comment_id, parentBlockContent, parentBlockId){
    let Html = "";
    if(parent_id != comment_id) {
        Html += "<img class='comment-block-reply-front-image' id='comment-block-reply-front-image-update' src= '/resources/png/right-and-down.png' /img>";
        Html += "<div class='comment-block-reply'>";
    }
    else Html += "<div class='comment-block'>"
    Html += "<h4 class = 'comment-headline-text-writer'> 댓글 수정 하기 </h4>"
         + "<textarea class='comment-write-content' placeholder='댓글 내용을 작성해주세요' id='comment-content-update' >"+parentBlockContent+"</textarea>"
         + "<div class='comment-write-bottom'>"
         + "<div class='comment-write-info'>"
         + "<input type='text' class='comment-write-name' placeholder='아이디' id='comment-writer-name-update' value = " + parentBlockId +">  </input>"
         + "<input type='password' class='comment-write-password' placeholder='비밀번호' id='comment-writer-password-update'>  </input>"
         + "</div>"
         + "<button class='comment-write-submit-button' id='comment-submit-update-cancel'> 취소 </button>"
         +  "<button class='comment-write-submit-button' id='comment-submit-update'> 댓글수정 </button>";
    Html += "</div>" + "</div>" + "</div>";
    return Html;
}

function createReplyCommentBlock(comment_id, parent_id, parent_html_id, type){
    cancelModify(type);

    let commentBlockList = document.getElementById("comment-block-list");
    let commentBlockOuter = document.createElement("div");
    let innerHTML = createReplyCommentHtmlCode(parent_id);

    commentBlockOuter.className = "comment-block-outer";
    commentBlockOuter.innerHTML = innerHTML;

    let nextBlock = document.getElementById("comment-block-outer-" + (parent_html_id+1));
    commentBlockList.insertBefore(commentBlockOuter, nextBlock);
    document.getElementById("comment-submit-reply-cancel").onclick = function(){ cancelModify(type) }
}

function createUpdateCommentBlock(comment_id, parent_id, parent_html_id, type){
    cancelModify(type);

    let commentBlockOuter = document.createElement("div");
    let parentBlockId = document.getElementById("comment-headline-text-writer-" + parent_html_id).textContent;
    let parentBlockContent = document.getElementById("comment-content-" + parent_html_id).textContent;
    let innerHTML = createUpdateCommentHtmlCode(parent_id, comment_id, parentBlockContent, parentBlockId);

    commentBlockOuter.className = "comment-block-update";
    commentBlockOuter.id = "comment-write-block-update";
    commentBlockOuter.innerHTML = innerHTML;

    let current_block = document.getElementById("comment-block-outer-" + (parent_html_id));
    let children = current_block.childNodes;
    for(let i=0; i<current_block.childElementCount;i++){
        let child = children[i];
        child.hidden = true;
    }

    current_block.appendChild(commentBlockOuter);
    currentUpdatingBlockHidden = current_block;
    document.getElementById("comment-submit-update").onclick = function(){ updateComment(comment_id); }
    document.getElementById("comment-submit-update-cancel").onclick = function(){ cancelModify(type) }
}

function createCommentBlock(comment, elementID){
    let commentBlockOuter = document.createElement("div");
    let commentBlock = document.createElement("div");
    let commentHeadline = document.createElement("div");
    const commentHeadlineTextWriter = document.createElement("h5");
    const commentHeadlineTextDate = document.createElement("p");
    const commentContent = document.createElement("div");
    const commentContentOuter = document.createElement("div");
    const commentModifyBlock = document.createElement("div");
    const commentModifyButton1 = document.createElement("button");
    const commentModifyButton2 = document.createElement("button");
    const commentModifyButton3 = document.createElement("button");


    commentBlockOuter.className = "comment-block-outer"
    //답글의 경우, 왼쪽에 화살표 이미지 삽입하는 코드
    if(comment.parent != comment.comment_id) {
        commentBlock.className = "comment-block-reply";
        // comment block 왼쪽에 공백 삽입
        let imageBlock = document.createElement("img");
        let commentBlockFront = document.createElement("div");
        imageBlock.src="/resources/png/right-and-down.png";
        imageBlock.className = "comment-block-reply-front-image";
        commentBlockFront.className = "comment-block-reply-front";
        commentBlockOuter.appendChild(imageBlock);
    }
    else commentBlock.className = "comment-block";

    commentHeadline.className = "comment-headline";
    commentHeadlineTextWriter.className = "comment-headline-text-writer";
    commentHeadlineTextDate.className = "comment-headline-text-date";
    commentContent.className = "comment-content";
    commentContentOuter.className = "comment-content-outer";
    commentModifyBlock.className = "comment-modify-block";
    commentModifyButton1.className = "comment-modify-buttons";
    commentModifyButton2.className = "comment-modify-buttons";
    commentModifyButton3.className = "comment-modify-buttons";
    commentModifyButton1.textContent = "수정";
    commentModifyButton2.textContent = "삭제";
    commentModifyButton3.textContent = "답글";

    commentModifyButton1.onclick = function() { createUpdateCommentBlock(comment.comment_id, comment.parent, elementID, "update")};
    commentModifyButton2.onclick = function() { openDeleteCommentWindow(comment.comment_id, comment.writer)};
    commentModifyButton3.onclick = function () { createReplyCommentBlock(comment.comment_id, comment.parent, elementID, "reply") }

    commentHeadlineTextWriter.textContent = comment.writer
    commentHeadlineTextDate.textContent = comment.date
    commentContent.textContent = comment.content

    commentBlockOuter.id = "comment-block-outer-" + elementID;
    commentHeadlineTextWriter.id = "comment-headline-text-writer-" + elementID;
    commentContent.id = "comment-content-" + elementID;

    commentHeadline.appendChild(commentHeadlineTextWriter);
    commentHeadline.appendChild(commentHeadlineTextDate);
    commentBlock.appendChild(commentHeadline);
    commentModifyBlock.append(commentModifyButton1);
    commentModifyBlock.append(commentModifyButton2);
    commentModifyBlock.append(commentModifyButton3);
    commentContentOuter.appendChild(commentContent);
    commentContentOuter.appendChild(commentModifyBlock);
    commentBlock.appendChild(commentContentOuter)
    commentBlockOuter.appendChild(commentBlock);

    return commentBlockOuter;
}

function createJsonTree(commentListArray){
    let tree = {};
    for(let i=0;i<commentListArray.length;i++){
        let curElem = commentListArray[i];
        let parent = curElem.parent;
        if(parent == curElem.comment_id) tree[curElem.comment_id] = [i];
        else {
            if(tree.hasOwnProperty(parent) == false) {
                // 부모가 없는 댓글 블록의 경우, 가상의 부모를 삽입해주기.
                tree["empty_parent"] = parent;
                tree[parent] = [i];
            }
            else tree[parent].push(i);
        }
    }
    return tree;
}