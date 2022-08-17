<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="/resources/write-board.css">
  <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
  <script src="/resources/js/general/functions.js"></script>
  <script src="/resources/js/board/boardRequest.js"></script>
  <!-- katex 추가하는 부분 -->
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
  <!-- katex 끝 -->
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

<!-- /resources/js/board.js 파일에 게시글 업로드 로직 존재 -->
<script>
  let boardID = ${board_id};
  let allImage = [];

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
          { type: 'html', content: body.content },
          { type: 'closeTag', tagName: 'div', outerNewLine: true }
        ];
      },
    },
    hooks: {
      addImageBlobHook: (file, callback) => {
        const formData = new FormData();
        formData.append("image", file);
        sendImage(formData, callback);
      }
    }
  });

  let type = "${type}";
  if(type == "update") {
    // 자바 코드를 통해 content를 불러오게 되면, 출력의 결과로 역슬래시가 2개에서 1개로 압축된 포함된 문자열에 str에 저장됨.
    // 역 슬래시가 한 개인 상태로 에디터로 넘어가면 역 슬래시가 아예 사라지는 문제 발생, 따라서 역슬래시 \\ 를 \\\\ 로 치환
    let str = `${fn:replace(content, '\\', '\\\\')}`;
    editor.setHTML(str);
  }
  document.getElementById("submit-button").onclick = function () { submit(type)};

  function submitTest(){
    document.getElementById("preview-Page").innerHTML = editor.getHTML();
    renderMathInElement(document.getElementById("preview-Page"), options);
  }

</script>
</html>
