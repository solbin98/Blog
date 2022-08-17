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
            if(resultCode == 1){
                updatePagingVo(res.pagingVo);
                setComments(res.comments);
            }
            else {
                alert(res.errorMessage);
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown){
            alert("통신 에러");
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
    let resultCode = ajaxForm("DELETE", url, {}, headers);
    if(resultCode == 1) win.close();
}

function updateComment(comment_id){
    let id = document.getElementById("comment-writer-name-update").value;
    let password = document.getElementById("comment-writer-password-update").value;
    let contents = document.getElementById("comment-content-update").value;
    let headerParams ={ id : encodeURI(id), password : encodeURI(password), content : encodeURI(contents)};
    let url = "/boards/" + boardID + "/comment/" + comment_id + "?page=" + pagingVo.nowPage;
    ajaxForm("PUT", url, {}, headerParams);
}

function loadComment(pageNumber){
    let url = "/boards/" + boardID + "/comment?page=" + pageNumber;
    ajaxForm("GET", url, {}, {});
}
