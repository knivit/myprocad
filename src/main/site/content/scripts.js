/** Display an image in a popup window */
function showImage(elem) {
    var w = 800;
    var h = 600;
    var left = (screen.width/2) - (w/2);
    var top = (screen.height/2) - (h/2);
    return window.open(elem.src, null, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
}
