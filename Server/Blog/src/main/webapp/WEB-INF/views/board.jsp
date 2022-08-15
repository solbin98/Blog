<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title> this is test message </title>
    <link href="/resources/myCss.css" rel="stylesheet" type="text/css" />
    <link href="/resources/sidebar.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
    <script src="//code.jquery.com/jquery.min.js"></script>
    <script src="/resources/js/category/loadCategory.js"></script>
    <script src="/resources/js/comment/commentBlocks.js"></script>
    <script src="/resources/js/comment/commentControl.js"></script>
    <script src="/resources/js/comment/commentRequest.js"></script>
    <script src="/resources/js/general/functions.js"></script>
    <script src="/resources/js/board/boardRequest.js"></script>
    <!-- katex start -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.11.1/dist/katex.min.css" integrity="sha384-zB1R0rpPzHqg7Kpt0Aljp8JPLqbXI3bhnPWROx27a9N0Ll6ZP/+DiW/UqRcLbRjq" crossorigin="anonymous"/>
    <script defer src="https://cdn.jsdelivr.net/npm/katex@0.11.1/dist/katex.min.js" integrity="sha384-y23I5Q6l+B6vatafAwxRu/0oK/79VlbSz7Q9aiSZUvyWYIYsd+qj+o24G5ZU2zJz" crossorigin="anonymous"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/katex@0.11.1/dist/contrib/auto-render.min.js" integrity="sha384-kWPLUVMOks5AQFrykwIup5lo0m3iMkkHrD0uJ4H5cjeGihAutqP0yW0J6dpFiVkI" crossorigin="anonymous" onload="renderMathInElement(document.body);"></script>
    <script>
        let options = {
            delimiters: [
                { left: "$$", right: "$$", display: true },
                { left: "$", right: "$", display: false },
            ],
            throwOnError : false
        }
        document.addEventListener("DOMContentLoaded", function () {
            renderMathInElement(document.body, options);
        });
    </script>
    <!-- katex end -->
</head>


<body style="background-color: #E8E8E8">
    <div class="RootDiv">
        <%@ include file="/resources/html/menuBar.html" %>

        <div class="content-side">
            <div class="board-block-full">
                <div class="board-headline" id="board-headline">
                    <h2 class="board-headline-title"> ${board.title} </h2>
                    <img src="/resources/png/delete.png" class="board-delete" id="board-delete">
                    <a href="/board-update-page/${board.board_id}" class="board-edit-outer" id="board-edit"> <img src="/resources/png/edit.png" class="board-edit" > </img> </a>
                    <p class="board-headline-date"> ${board.date} </p>
                </div>

                <div class="board-tagline" id = "board-tagline">
                    <div class="tag-front"> • 분류 : </div>
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
                        <button class="comment-write-submit-button" id="comment-submit" onclick="addComment(0,0)"> 댓글 작성 </button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</body>

<script>
    let pagingVo;
    let boardID = ${board.board_id};
    let admin = "<%= session.getAttribute("admin") %>";

    if(admin == "null" || admin == "false") {
        document.getElementById("write-board").hidden = true;
        let boardHeadline = document.getElementById("board-headline");
        boardHeadline.removeChild(document.getElementById("board-delete"));
        boardHeadline.removeChild(document.getElementById("board-edit"));
    }

    // 초기화 코드
    loadComment(1);
    loadCategory();

    function moveToNextPage(){
        if(pagingVo.nowPage+1 <= pagingVo.lastPage){
            loadComment(pagingVo.nowPage+1);
            resetPageNumbers();
            pagingVo.nowPage++;
        }
    }

    function moveToPrevPage(){
        if(pagingVo.nowPage-1 > 0){
            loadComment(pagingVo.nowPage-1);
            resetPageNumbers();
            pagingVo.nowPage--;
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

    function updatePagingVo(paging){
        if(paging != null){
            pagingVo = paging;
            resetPageNumbers();
        }
    }

    document.getElementById("board-delete").onclick = function () { deleteBoard(boardID) };
</script>
</html>
