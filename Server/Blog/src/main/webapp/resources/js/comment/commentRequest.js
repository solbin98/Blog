function ajaxForm(type, url, data, header){
    let resultCode = 0;
    $.ajax({
        type : type,
        url : url,
        data : data,
        headers : header,
        async: false,
        success : function(res){
            resultCode = res.resultCode;
            console.log("통신 성공!" + " result : " + resultCode);
            updatePagingVo(res.pagingVo);
            setComments(res.comments);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown){
            alert("서버와의 통신에 실패했습니다.")
        }
    });
    return resultCode;
}

function addComment(parent_id, comment_id){
    let dateString = getDateString();
    let writer = $("#comment-writer-name");
    let password = $("#comment-writer-password");
    let content = $("#comment-content");
    let depths = 1;
    let page = 0;

    if(parent_id != comment_id){
        writer = $("#comment-writer-name-reply");
        password = $("#comment-writer-password-reply");
        content = $("#comment-content-reply");
        depths = 2;
        page = pagingVo.nowPage;
    }

    const paramsForCommentWrite = {
          comment_id: 0
        , board_id: boardID
        , depth: depths
        , parent: parent_id
        , writer: writer.val()
        , password: password.val()
        , content: content.val()
        , date : dateString
    };

    ajaxForm("POST", "/boards/" + boardID + "/comment" +"?page=" + page , paramsForCommentWrite, {});
};

function deleteComment(comment_id, ids, pass, win){
    let url = "/boards/" + boardID + "/comment/" + comment_id + "?page=" + pagingVo.nowPage;
    let headers = { id : encodeURI(ids), password : encodeURI(pass) };
    let resultCode = ajaxForm("DELETE", url, {}, headers)
    if(resultCode == 1){
        alert("댓글이 삭제 되었습니다.");
        win.close();
    }
    else alert("비밀번호나 아이디가 잘못되었습니다.");
}

function updateComment(comment_id){
    let id = document.getElementById("comment-writer-name-update").value;
    let password = document.getElementById("comment-writer-password-update").value;
    let contents = document.getElementById("comment-content-update").value;
    let headerParams ={ id : encodeURI(id), password : encodeURI(password), content : encodeURI(contents)};
    let url = "/boards/" + boardID + "/comment/" + comment_id + "?page=" + pagingVo.nowPage;
    let resultCode = ajaxForm("PUT", url, {}, headerParams);
    if(resultCode == 1){ alert("댓글이 수정되었습니다."); }
    else alert("비밀번호나 아이디가 잘못되었습니다.");
}

function loadComment(pageNumber){
    let url = "/boards/" + boardID + "/comment?page=" + pageNumber;
    ajaxForm("GET", url, {}, {});
}
