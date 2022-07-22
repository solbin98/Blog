<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title> this is test message </title>
    <link href="/resources/myCss.css" rel="stylesheet" type="text/css" />
    <link href="/resources/sidebar.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
</head>
<script src="//code.jquery.com/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/latest/js/bootstrap.min.js"></script>

<body>
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
            <div class="content-side-headline"> <h3> ${pagingVo.nowPage} 페이지 </h3> </div>

            <c:forEach var="board" items="${boards}" varStatus="idx">
                <div class="board-block" id = "board-content-${board.board_id}">
                    <a href="/boards/${board.board_id}?category=${currentCategory}" class="a-color">
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
</html>
