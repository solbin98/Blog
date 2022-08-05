<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/resources/js/getNowDate.js"></script>
<script>
    function commentSubmit(parent_id){
        let dateString = getNowDate();

        let writer = $("#comment-writer-name");
        let password = $("#comment-writer-password");
        let content = $("#comment-content");
        let depths = 1;

        if(parent_id != 0){
            writer = $("#comment-writer-name-reply");
            password = $("#comment-writer-password-reply");
            content = $("#comment-content-reply");
            depths = 2;
        }

        const paramsForCommentWrite = {
            comment_id: 0
            , board_id: ${board.board_id}
            , depth: depths
            , parent: parent_id
            , writer: writer.val()
            , password: password.val()
            , content: content.val()
            , date : dateString
        };

        console.log(paramsForCommentWrite)
        // 댓글 작성을 위한 로직
        $.ajax({
            type : "POST",
            url : "/boards/" + ${board.board_id} + "/comment",
            data : paramsForCommentWrite,
            success : function(res){
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("통신 실패.")
            }
        });
    };

    function deleteComment(comment_id, ids, pass, win){
        console.log("넘어온 id : " + ids + " 비번 : " + pass);
        $.ajax({
            type : "DELETE",
            headers : { id : encodeURI(ids), password : encodeURI(pass) },
            url : "/boards/" + ${board.board_id} + "/comment/" + comment_id + "?page=" + pagingVo.nowPage,
            success : function(res){
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
                if(res.resultCode == 1){
                    win.alert("댓글이 삭제되었습니다.");
                    win.close();
                }
                else win.alert("비밀번호나 아이디가 잘못되었습니다.");
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                win.alert("댓글 삭제에 실패하였습니다.")
            }
        });
    }

    function updateComment(comment_id){
        let id = document.getElementById("comment-writer-name-update").value;
        let password = document.getElementById("comment-writer-password-update").value;
        let contents = document.getElementById("comment-content-update").value;
        let headerParams ={ id : encodeURI(id), password : encodeURI(password), content : encodeURI(contents)};

        console.log(JSON.stringify(headerParams));
        $.ajax({
            type : "PUT",
            headers : headerParams,
            url : "/boards/" + ${board.board_id} + "/comment/" + comment_id + "?page=" + pagingVo.nowPage,
            success : function(res){
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
                if(res.resultCode == 1){ alert("댓글이 수정되었습니다."); }
                else alert("비밀번호나 아이디가 잘못되었습니다.");
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("페이지 로딩에 실패하였습니다.")
            }
        });
    }

    function loadComment(pageNumber){
        // 댓글 조회를 위한 코드
        $.ajax({
            type : "GET",
            url : "/boards/" + ${board.board_id} + "/comment?page=" + pageNumber,
            success : function(res){
                updatePagingVo(res.pagingVo);
                loadComments(res.comments);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("페이지 로딩에 실패하였습니다.")
            }
        });
    }

    function deleteBoard(board_id){
        $.ajax({
            type : "DELETE",
            url : "/boards/" + ${board.board_id},
            success : function(res){
                alert("댓글 삭제에 성공 했습니다.")
                location.replace("/main");
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("댓글 삭제에 실패하였습니다.")
            }
        });
    }

</script>
