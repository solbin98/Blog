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

<body style="background: #E8E8E8">
    <div class="RootDiv">
        <!-- 사이드 메뉴바 -->
        <div id="page-wrapper">
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <h2 class="blog-title"> solbin98 </h2>
                    <c:forEach var="category" items="${categories}" varStatus="idx" >
                        <p class="category">
                            <a href="/category/${category.category_id}/boards" class="a-color"> ${category.name} (${category.total})</a>
                        </p>
                    </c:forEach>
                </ul>
            </div>
        </div>


        <div class="content-side">
            <div class="board-block-full">
                <div class="board-headline">
                    <h2 class="board-headline-title"> ${board.title} </h2>
                    <p class="board-headline-date"> ${board.date} </p>
                </div>

                <div class="board-tagline">
                    <p class="board-tagline-tag"> 수학 </p>
                    <p class="board-tagline-tag"> 그리디 </p>
                    <p class="board-tagline-tag"> 아이디어 </p>
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
                        <input class="comment-write-submit-button"  value="댓글 작성" id="comment-submit"> </input>
                    </div>
                </div>
            </div>
        </div>

    </div>
</body>

<script>
    let pagingVo;
    // 초기화 코드
    loadPage(1); // 댓글을 페이징으로 불러오는 함수 (인자 : page)

    $("#comment-submit").click(function(){
        var today = new Date();
        var year = today.getFullYear();
        var month = ('0' + (today.getMonth() + 1)).slice(-2);
        var day = ('0' + today.getDate()).slice(-2);
        var dateString = year + '-' + month  + '-' + day;

        const paramsForCommentWrite = {
            comment_id: 0
            , board_id: ${board.board_id}
            , depth: 1
            , parent: 0
            , writer: $("#comment-writer-name").val()
            , password: $("#comment-writer-password").val()
            , content: $("#comment-content").val()
            , date : dateString
        };

        console.log(paramsForCommentWrite)
        // 댓글 작성을 위한 로직
        $.ajax({
            type : "POST",
            url : "/boards/" + ${board.board_id} + "/comment",
            data : paramsForCommentWrite,
            success : function(res){
                document.getElementById("comment-write-block").scrollIntoView();
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("통신 실패.")
            }
        });
    });

    function loadPage(pageNumber){
        // 댓글 조회를 위한 로직
        $.ajax({
            type : "GET",
            url : "/boards/" + ${board.board_id} + "/comment?page=" + pageNumber,
            success : function(res){
                document.getElementById("comment-count").scrollIntoView();
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("페이지 로딩에 실패하였습니다.")
            }
        });
    }

    function moveToNextPage(){
        if(pagingVo.nowPage+1 <= pagingVo.lastPage){
            loadPage(pagingVo.nowPage+1);
            resetPageNumbers();
        }
    }

    function moveToPrevPage(){
        if(pagingVo.nowPage-1 > 0){
            loadPage(pagingVo.nowPage-1);
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
            newNumber.setAttribute("onclick", "loadPage("+i+")")
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
        // empty-parent가 존재하는 경우에 대한 처리.
        if(tree.hasOwnProperty("empty_parent")){
            let parentID = tree["empty_parent"];
            let arrSize = tree[parentID].length;
            for(let j=0;j<arrSize;j++){
                let nowIdx = tree[parentID][j];
                createElement(commentListArray, nowIdx);
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
                createElement(commentListArray, nowIdx);
            }
        }
    }

    function createElement(commentListArray, nowIdx){
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
        //css 적용을 위해 클래스 부여
        if(commentListArray[nowIdx].parent != 0) {
            commentBlock.className = "comment-block-reply";
            // comment block 왼쪽에 공백 삽입
            let imageBlock = document.createElement("img");
            let commentBlockFront = document.createElement("div");
            imageBlock.src="/resources/png/right-and-down.png";
            imageBlock.className = "comment-block-reply-front-image";
            commentBlockFront.className = "comment-block-reply-front";
            //commentBlockFront.appendChild(imageBlock);
            commentBlockOuter.appendChild(imageBlock);
        }
        else {
            commentBlock.className = "comment-block";
        }
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

        commentModifyButton1.setAttribute("onclick", "updateComment("+commentListArray[nowIdx].comment_id+")");
        commentModifyButton2.setAttribute("onclick", "deleteComment("+commentListArray[nowIdx].comment_id+")");
        commentModifyButton3.setAttribute("onclick", "deleteComment("+commentListArray[nowIdx].comment_id+")");

        //엘리먼트 간의 계층 관계 설정
        commentHeadlineTextWriter.textContent = commentListArray[nowIdx].writer
        commentHeadlineTextDate.textContent = commentListArray[nowIdx].date
        commentContent.textContent = commentListArray[nowIdx].content

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
        console.log("tree 구조 : " + JSON.stringify(tree));
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

    function checkWriterInfo(comment_id, id, pass, win){
        let result = 0;
        console.log("전달된 아이디 : " + id, " 비번 : ", pass);
        let paramsForChecking = { id : id ,password : pass };
        $.ajax({
            type : "POST",
            data : paramsForChecking,
            url : "/comment/"+comment_id+"/writerInfo",
            success : function(res){
                if(res.checkResult == 1) {
                    console.log(result + "출력해보리기2");
                    $.ajax({
                        type : "DELETE",
                        url : "/boards/" + ${board.board_id} + "/comment/" + comment_id + "?page=" + pagingVo.nowPage,
                        success : function(res){
                            updatePagingVo(res.pagingVo);
                            loadComments(res.comments);
                            alert("댓글이 삭제되었습니다.")
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown){
                            alert("댓글을 삭제에 실패하였습니다.")
                        }
                    });
                }
                else window.alert("비밀번호나 아이디가 잘못되었습니다.");
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                win.alert("정보 체크에 실패하였습니다.")
                win.close();
            }
        });

        win.close();
    }

    function close(win){
        win.close();
    }

    function deleteComment(comment_id){
        let win = window.open("", "댓글 삭제", "width=500,height=150");
        win.document.write(
            "<h5> 댓글을 삭제하기 위해서 아이디와 비밀번호를 입력해주세요. </h5> <input type='text' placeholder='아이디' id='id-text'> </input> <input type='password' placeholder='비밀번호' id='password-text'> </input> " +
            "<p> <button id='delete-button'> 삭제하기 </button> <button id='close-button'> 취소하기 </button> </p>",
        );

        let delete_button = win.document.getElementById("delete-button");
        let close_button = win.document.getElementById("close-button");
        let id_input = win.document.getElementById("id-text")
        let pass_input = win.document.getElementById("password-text");
        delete_button.onclick = function() { checkWriterInfo(comment_id, id_input.value, pass_input.value, win); }
        close_button.onclick = function () { close(win) };
    }

    function updateComment(comment_id){
        // 댓글 수정을 위한 로직
        let paramsForCommentUpdate = {
            "comment_id": comment_id,
            "board_id": 1,
            "depth": 1,
            "parent": 1,
            "writer": 1,
            "password": 1,
            "content": 1,
            "date": 1
        }

        $.ajax({
            type : "PUT",
            data : paramsForCommentUpdate,
            url : "/boards/" + ${board.board_id} + "/comment/" + comment_id + "?page=" + pagingVo.nowPage,
            success : function(res){
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("페이지 로딩에 실패하였습니다.")
            }
        });
    }

</script>
</html>
