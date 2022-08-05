<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title> this is test message </title>
    <link href="/resources/myCss.css" rel="stylesheet" type="text/css" />
    <link href="/resources/sidebar.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
    <script src="//code.jquery.com/jquery.min.js"></script>
    <script src="/resources/js/createNavBar.js"></script>
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
            <div class="content-side-headline"> <h3> ${pagingVo.nowPage} 페이지 </h3> </div>

            <c:forEach var="board" items="${boards}" varStatus="idx">
                <div class="board-block" id = "board-content-${board.board_id}">
                    <a href="/boards/${board.board_id}?category=${currentCategory}" class="a-color">
                        <div class="board-headline">
                            <h2 class="board-headline-title"> ${board.title} </h2>
                            <p class="board-headline-date"> ${board.date} </p>
                        </div>

                        <div class="board-tagline" id="board-tagline-${board.board_id}">
                            <div class="tag-front"> • </div>
                            <c:forEach var="tagID" items="${tags}">
                                <c:if test="${tagID.key eq board.board_id}">
                                    <c:forEach var="tagName" items="${tagID.value}">
                                        <div class="tag-box"> ${tagName.name}</div>
                                    </c:forEach>
                                </c:if>
                            </c:forEach>
                        </div>

                        <div class="board-content">${board.content}</div>
                    </a>
                </div>
            </c:forEach>

            <div class="page-number-div-outer" id="page-numbers">
                <div class="page-number-div-inner">
                    <a class="page-next" href="/category/${currentCategory}/boards?page=${pagingVo.nowPage-1}">
                        <img src="/resources/png/left.png" class="page-next"></a>
                    <c:forEach begin="${pagingVo.leftMostPage}" end="${pagingVo.rightMostPage}" varStatus="number">
                        <a href="/category/${currentCategory}/boards?page=${number.current}" class="page-number"> ${number.current} </a>
                    </c:forEach>
                    <a class="page-next" href="/category/${currentCategory}/boards?page=${pagingVo.nowPage+1}">
                        <img src="/resources/png/right.png" class="page-next"></a>
                </div>
            </div>
        </div>
    </div>

</body>
<script>
    let admin = "<%= session.getAttribute("admin") %>";
    if(admin == "null" || admin == "false") document.getElementById("write-board").hidden = true;

    loadCategory();
</script>
</html>
