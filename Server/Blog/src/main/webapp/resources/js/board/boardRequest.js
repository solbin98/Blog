
function sendImage(formData, callback){
    $.ajax({
        type: 'POST',
        url: '/boards/image',
        data: formData,
        dataType: 'json',
        processData: false,
        contentType: false,
        cache: false,
        timeout: 6000000,
        success: function(data) {
            allImage.push(data.name);
            let url = '/images/' + data.name;
            callback(url, '이미지 추가');
        },
        error: function(e) {
            callback('image_load_fail', '사진 대체 텍스트 입력');
        }
    });
}

function submit(){
    let htmlCode = editor.getHTML();
    let images = getImageFileNamesStringFromHtmlCode(htmlCode);
    let categoryID = document.getElementById("category-select").value;
    let title = document.getElementById("title").value;
    let date = getDateString();
    let tag = document.getElementById("tag-text").value.split(" ");
    let board_id = boardID;
    let data = {
        "image": images,
        "allImage": allImage,
        "content": htmlCode,
        "categoryID": categoryID,
        "title": title,
        "date": date,
        "tag": tag,
        "type" : type,
        "board_id" : board_id
    };

    $.ajax({
        type : "POST",
        url : "/boards",
        data : data,
        success : function(res){
            console.log(res);
            if(res.resultCode) {
                alert("게시글 작성에 성공했습니다.");
                location.replace("/main");
            }
            else{
                let errorMessage = res.errorMessage;
                alert(errorMessage);
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown){
            alert("카테고리 로딩 실패");
        }
    });
}

function deleteBoard(boardID){
    $.ajax({
        type : "DELETE",
        url : "/boards/" + boardID,
        success : function(res){
            if(res.resultCode) {
                alert("게시글 삭제에 성공했습니다.");
                location.replace("/main");
            }
            else{
                alert("게시글 삭제에 실패했습니다.");
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown){
            alert("통신 실패");
        }
    });
}