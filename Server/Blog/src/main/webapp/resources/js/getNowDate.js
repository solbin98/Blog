function getNowDate(){
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