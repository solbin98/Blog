function createTagElement(tagName){
    let tagBox = document.createElement("div");
    tagBox.className = "tag-box";
    tagBox.textContent = tagName;
    return tagBox;
}