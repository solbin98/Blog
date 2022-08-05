
function makeJsonTypeCategoryTree(categoryListArray){
    let tree = {};
    for(let i=0;i<categoryListArray.length;i++){
        console.log(categoryListArray[i]);
    }
    for(let i=0;i<categoryListArray.length;i++){
        let curElem = categoryListArray[i];
        let parent = curElem.parent;
        let categoryID = curElem.category_id;
        if(parent == 0) tree[categoryID] = [i];
    }

    for(let i=0;i<categoryListArray.length;i++) {
        let curElem = categoryListArray[i];
        let parent = curElem.parent;
        if(parent != 0) tree[parent].push(i);
    }
    return tree;
}

function addCategoryToNavBar(categoryListArray, tree, categoryUl){
    for(let i=0;i<categoryListArray.length;i++){
        let curElem = categoryListArray[i];
        let parent = curElem.parent;
        let parentCategoryID = curElem.category_id;

        if(parent != 0) continue;
        let length = tree[parentCategoryID].length

        for(let j=0;j<length;j++){
            let index = tree[parentCategoryID][j];
            let nowElem = categoryListArray[index];
            let parentID = nowElem.parent;
            let categoryID = nowElem.category_id;
            let categoryName = nowElem.name;
            let categoryTotal = nowElem.total;

            let categoryP  = document.createElement("div");
            let categoryA = document.createElement("a");
            let categoryC = document.createElement("img");

            if(parentID != 0){
                categoryC.className = "category-front";
                categoryC.src = "/resources/png/right-and-down.png";
                categoryP.appendChild(categoryC);
            }

            categoryP.className = "category";
            categoryA.className = "a-color";
            categoryA.href = "/category/" + categoryID + "/boards";
            categoryA.textContent = ((parentID == 0)? '• ' : '') + categoryName + " (" +  categoryTotal + ")";


            categoryP.appendChild(categoryA);
            categoryUl.appendChild(categoryP);
        }
    }
}


function getCategories(){
    let categories = null;
    $.ajax({
        type : "GET",
        url : "/category",
        async : false,
        success : function(res){
            categories = res.categories;
        },
        error : function(XMLHttpRequest, textStatus, errorThrown){
            alert("카테고리 로딩 실패");
        }
    });
    return categories;
}

function loadCategory(){
    let categoryListArray = getCategories();
    if(categoryListArray == null){
        alert("카테고리 로딩에 실패했습니다.");
        return;
    }
    let tree = makeJsonTypeCategoryTree(categoryListArray);
    let categoryUl = document.getElementById("sidebar-nav");
    addCategoryToNavBar(categoryListArray, tree, categoryUl);
}
