function loadBoardTags(board_id){
    let tagLine = document.getElementById("board-tagline");
    let tags = loadTags(board_id);
    for(let i=0;i<tags.length;i++){
        let tagName = tags[i].name;
        let newTagBox = createTagElement(tagName);
        tagLine.appendChild(newTagBox);
    }
}