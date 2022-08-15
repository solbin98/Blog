let currentUpdatingBlockHidden;
let deleteWindow;

function clearComments(){
    const doc = document;
    let commentList =  doc.getElementById("comment-block-list");
    while(commentList.hasChildNodes()){
        commentList.removeChild(commentList.firstChild)
    }
}

function setComments(commentListArray){
    clearComments();
    let tree = createJsonTree(commentListArray);
    addCommentElements(commentListArray, tree);
}

function cancelModify(type){
    if(type == "reply"){
        let prev = document.getElementById("comment-write-block-reply");
        if(prev != null) {
            document.getElementById("comment-block-reply-front-image-reply").remove();
            prev.remove();
        }
    }
    else if(type == "update"){
        let prev = document.getElementById("comment-write-block-update");
        if(prev != null) {
            document.getElementById("comment-write-block-update").remove();
            if(document.getElementById("comment-block-reply-front-image-update") != null)
                document.getElementById("comment-block-reply-front-image-update").remove();
            let current_block = currentUpdatingBlockHidden;
            if(current_block != null) {
                let children = current_block.childNodes;
                for (let i = 0; i < current_block.childElementCount; i++) {
                    let child = children[i];
                    child.hidden = false;
                }
            }
        }
    }
}

function addCommentElements(commentListArray, tree) {
    let commentBlockList = document.getElementById("comment-block-list");
    let elementID = 1;

    // empty-parent가 존재하는 경우에 대한 처리.
    if(tree.hasOwnProperty("empty_parent")){
        let parentID = tree["empty_parent"];
        let arrSize = tree[parentID].length;
        for(let j=0;j<arrSize;j++){
            let nowIdx = tree[parentID][j];
            let newBlock = createCommentBlock(commentListArray[nowIdx], elementID++);
            commentBlockList.appendChild(newBlock);
        }
    }

    for(let i=0; i < commentListArray.length; i++){
        let curElem = commentListArray[i];
        if(curElem.depth != 1) continue;
        let parentID = curElem.comment_id;
        let arrSize = tree[parentID].length;
        for(let j=0;j<arrSize;j++) {
            let nowIdx = tree[parentID][j]; // nowIdx -> 현재 가르키는 원소의 commentListArray 에서의 인덱스
            if (commentListArray[nowIdx].parent ==  tree["empty_parent"]) continue;
            let newBlock = createCommentBlock(commentListArray[nowIdx], elementID++);
            commentBlockList.appendChild(newBlock);
        }
    }
}

function openDeleteCommentWindow(comment_id, writer){
    console.log(comment_id + " , " + writer);

    if(deleteWindow != null) deleteWindow.close();
    deleteWindow = window.open("", "댓글 삭제", "width=500,height=150");
    deleteWindow.document.write(
        "<h5> 댓글을 삭제하기 위해서 아이디와 비밀번호를 입력해주세요. </h5> <input type='text' placeholder='아이디' id='id-text' value='"+writer+"' > </input> <input type='password' placeholder='비밀번호' id='password-text'> </input> " +
        "<p> <button id='delete-button'> 삭제하기 </button> <button id='close-button'> 취소하기 </button> </p>",
    );

    let delete_button = deleteWindow.document.getElementById("delete-button");
    let close_button = deleteWindow.document.getElementById("close-button");
    let id_input = deleteWindow.document.getElementById("id-text");
    let pass_input = deleteWindow.document.getElementById("password-text");
    console.log(comment_id + " 를 삭제 할 것");
    delete_button.onclick = function() { deleteComment(comment_id, id_input.value, pass_input.value, deleteWindow)}
    close_button.onclick = function () { deleteWindow.close() };
}

