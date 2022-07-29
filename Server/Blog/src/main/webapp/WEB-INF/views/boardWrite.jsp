<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
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
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<script src="//code.jquery.com/jquery.min.js"></script>
<body>
<div id="editor"></div>
<button onclick="submit()"> 게시글 작성 </button>
<p id="preview" >  </p>


</body>


<script>
  let allImage = [];
  const Editor = toastui.Editor;
  const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    hooks: {
      addImageBlobHook: (file, callback) => {
        // blob : Java Script 파일 객체
        //console.log(blob);
        console.log(file);
        const formData = new FormData();
        formData.append("image", file);
        let url = '/images/';
        $.ajax({
          type: 'POST',
          url: 'boards/image',
          data: formData,
          dataType: 'json',
          processData: false,
          contentType: false,
          cache: false,
          timeout: 6000000,
          success: function(data) {
            allImage.push(data.name);
            url += data.name;
            callback(url, '이미지 추가');
          },
          error: function(e) {
            callback('image_load_fail', '사진 대체 텍스트 입력');
          }
        });
      }
    }
  });

  function getImageFileNames(htmlCode){
      let names = [];
      let tmpDoc = document.createElement('div');
      tmpDoc.innerHTML = htmlCode;
      let images = tmpDoc.getElementsByTagName("img");
      for(let i=0;i<images.length;i++){
        let src = images[i].src;
        let imageFileName = "";
        for(let i=src.length-1;i>=0;i--) {
          if(src[i] == "/") break;
          imageFileName += src[i];
        }
        imageFileName = imageFileName.split('').reverse().join('');
        names.push(imageFileName);
      }
      return names;
  }

  function boardSubmit(htmlCode, images, allImage, category, title, date, tag) {
    $.ajax({
      type: 'POST',
      url: 'boards',
      data: {
        "image": images,
        "allImage": allImage,
        "content": htmlCode,
        "categoryID": category,
        "title": title,
        "date": date,
        "tag": tag
      },
      dataType: 'json',
      success: function (data) {

      },
      error: function (e) {

      }
    });
  }

  function submit(){
    let htmlCode = editor.getHTML();
    let images = getImageFileNames(htmlCode);
    let categoryID = 1;
    let title = "테스트 게시글";
    let date = "2022-07-29";
    let tag = [1,2];

    boardSubmit(htmlCode, images, allImage, categoryID, title, date, tag);
  }

</script>
</html>
