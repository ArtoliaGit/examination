/**
 * @param {*} obj1 对象
 * @param {*} obj2 对象
 * @description 判断两个对象是否相等，这两个对象的值只能是数字或字符串
 */
export const objEqual = (obj1, obj2) => {
  const keysArr1 = Object.keys(obj1);
  const keysArr2 = Object.keys(obj2);
  if (keysArr1.length !== keysArr2.length) return false;
  if (keysArr1.length === 0 && keysArr2.length === 0) return true;
  /* eslint-disable-next-line */
  else return !keysArr1.some(key => obj1[key] != obj2[key])
};

/**
 * @returns {String} 当前浏览器名称
 */
export function getExplorer() {
  const ua = window.navigator.userAgent;
  const isExplorer = exp => ua.indexOf(exp) > -1;
  let type = '';
  if (isExplorer('MSIE')) type = 'IE';
  if (isExplorer('Firefox')) type = 'Firefox';
  if (isExplorer('Chrome')) type = 'Chrome';
  if (isExplorer('Opera')) type = 'Opera';
  if (isExplorer('Safari')) type = 'Safari';
  return type;
}

function formatNumber(n) {
  const str = n.toString();
  return str[1] ? str : `0${str}`;
}

/**
 * 格式化日期
 * @param {Date} date
 */
export function formatTime(date) {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();

  const t1 = [year, month, day].map(formatNumber).join('-');
  const t2 = [hour, minute, second].map(formatNumber).join(':');

  return `${t1} ${t2}`;
}
