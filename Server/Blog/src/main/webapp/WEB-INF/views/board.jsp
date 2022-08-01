<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title> this is test message </title>
    <link href="/resources/myCss.css" rel="stylesheet" type="text/css" />
    <link href="/resources/sidebar.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
</head>
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/latest/js/bootstrap.min.js"></script>
<script>
    MathJax = {
        tex: { inlineMath: [['$', '$'], ['\\(', '\\)']] },
        svg: { fontCache: 'global' }
    };
</script>
<script src="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-chtml.js" id="MathJax-script"></script>


<body style="background-color: #E8E8E8">
    <div class="RootDiv">
        <%@ include file="/resources/html/menuBar.html" %>

        <div class="content-side">
            <div class="board-block-full">
                <div class="board-headline">
                    <h2 class="board-headline-title"> ${board.title} </h2>
                    <img src="/resources/png/delete.png" class="board-delete" id="board-delete">
                    <a href="/board-update-page/${board.board_id}" class="board-edit"> <img src="/resources/png/edit.png" class="board-edit" id="edit"> </img> </a>
                    <p class="board-headline-date"> ${board.date} </p>
                </div>

                <div class="board-tagline" id = "board-tagline">
                    <c:forEach var="tag" items="${tags}">
                        <div class="tag-box">${tag.name}</div>
                    </c:forEach>
                </div>

                <div class="board-content">${board.content}</div>
            </div>

            <div class="comment-side">
                <div class="comment-side-headline" id="comment-count"> 댓글 (${commentTotal}) </div>

                <!-- 동적으로 댓글 블록들이 추가되는 곳 -->

                <div id="comment-block-list">

                </div>

                <div class="page-number-div-outer" id="page-numbers">
                    <div class="page-number-div-inner">
                        <img src="/resources/png/left.png" class="page-next" onclick="moveToPrevPage()">
                        <!-- 동적으로 페이지 넘버들이 채워지는 곳-->
                        <div class="page-number-div-list" id="page-number-div-list">

                        </div>
                        <img src="/resources/png/right.png" class="page-next" onclick="moveToNextPage()">
                    </div>
                </div>

                <div class="comment-write-block" id="comment-write-block">
                    <textarea class="comment-write-content" placeholder="댓글 내용을 작성해주세요" id="comment-content"></textarea>
                    <div class="comment-write-bottom">
                        <div class="comment-write-info">
                            <input type="text" class="comment-write-name" placeholder="아이디" id="comment-writer-name">  </input>
                            <input type="password" class="comment-write-password" placeholder="비밀번호" id="comment-writer-password">  </input>
                        </div>
                        <button class="comment-write-submit-button" id="comment-submit" onclick="commentSubmit(0)"> 댓글 작성 </button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</body>
<%@ include file="/resources/jsp/request.jsp" %>
<script>
    let pagingVo;
    let currentUpdatingBlockHidden;
    let deleteWindow;
    // 초기화 코드
    loadComment(1); // 댓글을 페이징으로 불러오는 함수 (인자 : page)

    function moveToNextPage(){
        if(pagingVo.nowPage+1 <= pagingVo.lastPage){
            loadComment(pagingVo.nowPage+1);
            resetPageNumbers();
        }
    }

    function moveToPrevPage(){
        if(pagingVo.nowPage-1 > 0){
            loadComment(pagingVo.nowPage-1);
            resetPageNumbers();
        }
    }

    function resetPageNumbers(){
        const doc = document;
        let pageNumberList = doc.getElementById("page-number-div-list");
        doc.getElementById("comment-count").textContent = "댓글(" + pagingVo.total + ")";
        while(pageNumberList.hasChildNodes()){
            pageNumberList.removeChild(pageNumberList.firstChild);
        }

        for(let i=pagingVo.leftMostPage;i<=pagingVo.rightMostPage;i++){
            let newNumber = document.createElement("a");
            newNumber.className = "page-number";
            newNumber.textContent = i;
            newNumber.setAttribute("onclick", "loadComment("+i+")")
            pageNumberList.appendChild(newNumber);
        }
    }

    function initComments(){
        const doc = document;
        let commentList =  doc.getElementById("comment-block-list");
        while(commentList.hasChildNodes()){
            commentList.removeChild(commentList.firstChild)
        }
    }

    function addCommentElement(commentListArray,tree) {
        let elementID = 1;
        console.log(JSON.stringify(tree));
        // empty-parent가 존재하는 경우에 대한 처리.
        if(tree.hasOwnProperty("empty_parent")){
            let parentID = tree["empty_parent"];
            let arrSize = tree[parentID].length;
            for(let j=0;j<arrSize;j++){
                let nowIdx = tree[parentID][j];
                createCommentBlock(commentListArray, nowIdx, elementID++);
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
                createCommentBlock(commentListArray[nowIdx], elementID++);
            }
        }
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

    function createWriteCommentBlock(comment_id, parent_id, parent_html_id, type){
        cancelModify(type);

        let commentBlockOuter = document.createElement("div");
        let innerHTML = "";

        if(type == "reply") {
            commentBlockOuter.className = "comment-block-outer";
            if(parent_id == 0) parent_id = comment_id;
            innerHTML += "<img class='comment-block-reply-front-image' id='comment-block-reply-front-image-reply' src= '/resources/png/right-and-down.png' /img>";
            innerHTML += "<div class='comment-block-reply' id='comment-write-block-reply'>";
            innerHTML += "<h4 class = 'comment-headline-text-writer'> 답글 추가 하기 </h4>";
            innerHTML += "<textarea class='comment-write-content' placeholder='댓글 내용을 작성해주세요' id='comment-content-reply'></textarea>"
            innerHTML += "<div class='comment-write-bottom'>"
                        + "<div class='comment-write-info'>"
                        + "<input type='text' class='comment-write-name' placeholder='아이디' id='comment-writer-name-reply'> </input>"
                        + "<input type='password' class='comment-write-password' placeholder='비밀번호' id='comment-writer-password-reply'>  </input>"
                        + "</div>"
                        + "<button class='comment-write-submit-button' id='comment-submit-reply-cancel'> 취소 </button>"
            innerHTML +=  "<button class='comment-write-submit-button' id='comment-submit-reply' onclick='commentSubmit(" + parent_id + ")'> 답글작성 </button>";
        }
        else if(type == "update"){
            commentBlockOuter.className = "comment-block-update";
            commentBlockOuter.id = "comment-write-block-update";

            let parentBlockId = document.getElementById("comment-headline-text-writer-" + parent_html_id).textContent;
            let parentBlockContent = document.getElementById("comment-content-" + parent_html_id).textContent;

            if(parent_id != 0) {
                innerHTML += "<img class='comment-block-reply-front-image' id='comment-block-reply-front-image-update' src= '/resources/png/right-and-down.png' /img>";
                innerHTML += "<div class='comment-block-reply'>";
            }
            else innerHTML += "<div class='comment-block'>"
            innerHTML  += "<h4 class = 'comment-headline-text-writer'> 댓글 수정 하기 </h4>"
                       + "<textarea class='comment-write-content' placeholder='댓글 내용을 작성해주세요' id='comment-content-update' >"+parentBlockContent+"</textarea>"
                       + "<div class='comment-write-bottom'>"
                       + "<div class='comment-write-info'>"
                       + "<input type='text' class='comment-write-name' placeholder='아이디' id='comment-writer-name-update' value = " + parentBlockId +">  </input>"
                       + "<input type='password' class='comment-write-password' placeholder='비밀번호' id='comment-writer-password-update'>  </input>"
                       + "</div>"
                       + "<button class='comment-write-submit-button' id='comment-submit-update-cancel'> 취소 </button>"
                       +  "<button class='comment-write-submit-button' id='comment-submit-update'> 댓글수정 </button>";
        }
        innerHTML += "</div>" + "</div>" + "</div>"

        commentBlockOuter.innerHTML = innerHTML;
        let commentBlockList = document.getElementById("comment-block-list");
        if(type == "update"){
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
        else{
            let nextBlock = document.getElementById("comment-block-outer-" + (parent_html_id+1));
            commentBlockList.insertBefore(commentBlockOuter, nextBlock);
            document.getElementById("comment-submit-reply-cancel").onclick = function(){ cancelModify(type) }
        }
    }

    function createCommentBlock(comment, elementID){
        let commentBlockList = document.getElementById("comment-block-list");

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
        if(comment.parent != 0) {
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

        commentModifyButton1.onclick = function() { createWriteCommentBlock(comment.comment_id, comment.parent, elementID, "update")};
        commentModifyButton2.onclick = function() { openDeleteCommentWindow(comment.comment_id, comment.writer)};
        commentModifyButton3.onclick = function () { createWriteCommentBlock(comment.comment_id, comment.parent, elementID, "reply") }

        //엘리먼트 간의 계층 관계 설정
        commentHeadlineTextWriter.textContent = comment.writer
        commentHeadlineTextDate.textContent = comment.date
        commentContent.textContent = comment.content

        //id를 설정해주기
        commentBlockOuter.id = "comment-block-outer-" + elementID;
        commentHeadlineTextWriter.id = "comment-headline-text-writer-" + elementID;
        commentContent.id = "comment-content-" + elementID;

        //삽입
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

        //댓글 리스트 div 에 추가
        commentBlockList.appendChild(commentBlockOuter);
    }

    function makeJsonTree(commentListArray){
        let tree = {};
        for(let i=0;i<commentListArray.length;i++){
            let curElem = commentListArray[i];
            let parent = curElem.parent;
            if(parent == 0) tree[curElem.comment_id] = [i];
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

    function loadComments(commentListArray){
        initComments();
        let tree = makeJsonTree(commentListArray);
        addCommentElement(commentListArray, tree);
    }

    function updatePagingVo(paging){
        pagingVo = paging;
        resetPageNumbers();
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
        console.log(id_input);
        delete_button.onclick = function() { deleteComment(comment_id, id_input.value, pass_input.value, deleteWindow)}
        close_button.onclick = function () { deleteWindow.close() };
    }

    document.getElementById("board-delete").onclick = function () { deleteBoard(${board.board_id}) }
</script>

</html>
