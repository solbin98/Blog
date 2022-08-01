<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="/resources/write-board.css">
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
      //renderMathInElement(document.body, options);
    });
  </script>
  <!-- katex end -->
</head>
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<script src="//code.jquery.com/jquery.min.js"></script>
<body>
  <h2 class="title-text"> 제목 </h2>
  <input class="title" type="text" placeholder="게시글 제목을 입력해주세요." id="title" value="${title}">
  <div class="editor" id="editor"></div>

  <div class="category-box">
    <h3 class="category-text"> 카테고리 </h3>
    <select class="category-select" id="category-select" value="${categoryID}">
      <c:forEach var="category" items="${categories}" varStatus="idx" >
        <option value="${category.category_id}"> ${category.name} </option>
      </c:forEach>
    </select>
  </div>

  <div class="tag-box">
    <h3 class="tag-text"> 태그선택 </h3>
    <input class="tag-input" type="text" placeholder="태그를 입력해주세요" id="tag-text" value="${tags}">
  </div>

  <div class="submit-button-div">
    <button onclick="submit()" class="submit-button" id="submit-button"> 게시글 작성 </button>
  </div>

  <div class="submit-button-div">
    <button onclick="submitTest()" class="submit-button"> 게시글 미리보기 </button>
  </div>

  <p id="preview-Page"></p>
</body>


<script>
  let board_id = 0;
  let allImage = [];
  let type = "${type}";
  document.getElementById("submit-button").onclick = function () { submit(type)};

  const Editor = toastui.Editor;
  const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    customHTMLRenderer: {
      latex(node) {
        const generator = new latexjs.HtmlGenerator({ hyphenate: false });
        const { body } = latexjs.parse(node.literal, { generator }).htmlDocument();

        return [
          { type: 'openTag', tagName: 'div', outerNewLine: true },
          { type: 'html', content: body.innerHTML },
          { type: 'closeTag', tagName: 'div', outerNewLine: true }
        ];
      },
    },
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

  let string
  if(type == "update") {
    editor.setHTML('${content.replace("\\","\\\\")}');
  }



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

  function boardSubmit(htmlCode, images, allImage, category, title, date, tag, type, board_id) {
    $.ajax({
      type: 'POST',
      url: '/boards',
      data: {
        "image": images,
        "allImage": allImage,
        "content": htmlCode,
        "categoryID": category,
        "title": title,
        "date": date,
        "tag": tag,
        "type" : type,
        "board_id" : board_id
      },
      dataType: 'json',
      success: function (data) {
        alert("게시글 작성에 성공했습니다.");
        location.replace("main");
      },
      error: function (e) {
        alert("게시글 작성에 실패했습니다.");
      }
    });
  }

  function submit(){
    let htmlCode = editor.getHTML();
    let images = getImageFileNames(htmlCode);
    let categoryID = document.getElementById("category-select").value;
    let title = document.getElementById("title").value;
    let date = "2022-07-29";
    let tag = document.getElementById("tag-text").value.split(" ");
    let board_id = ${board_id};
    boardSubmit(htmlCode, images, allImage, categoryID, title, date, tag, type, board_id);
  }

  function submitTest(){
    document.getElementById("preview-Page").innerHTML = editor.getHTML();
    renderMathInElement(document.getElementById("preview-Page"), options);
  }

</script>
</html>
