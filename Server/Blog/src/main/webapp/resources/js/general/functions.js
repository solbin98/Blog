function getDateString(){
    let today = new Date();
    let year = today.getFullYear();
    let month = ('0' + (today.getMonth() + 1)).slice(-2);
    let day = ('0' + today.getDate()).slice(-2);
    let hour = ('0' + today.getHours()).slice(-2);
    let minute = ('0' + today.getMinutes()).slice(-2);
    let second = ('0' + today.getSeconds()).slice(-2);
    let dateString = year + '-' + month  + '-' + day + " " + hour + ":" + minute + ":" + second;
    return dateString;
}

function getImageFileNamesStringFromHtmlCode(htmlCode){
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

function getErrorMessage(errorMessage){
    let message = "";
    for(let i=0;i<errorMessage.length;i++){
        message += errorMessage[i] + "\n";
    }
    return message;
}
