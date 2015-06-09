// Преобразовывает число в строку с s знаками после запятой
function toStr(num, s) {
  return round(num, s).toString(10);
}

function round(num, s) {
  var sc = Math.pow(10, s);
  return (Math.round(num*sc)/sc);
}

function toDegrees(radians) {
    return radians * 180 / Math.PI;
}

function toRadians(degrees) {
    return degrees * Math.PI / 180;
}
