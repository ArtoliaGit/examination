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
export function formatTime(date, pattern) {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();

  let result = pattern;
  if (pattern.includes('yyyy')) {
    result = result.replace(/yyyy/g, year);
  }
  if (pattern.includes('yy')) {
    result = result.replace(/yy/g, year.toString().substr(-2));
  }
  if (pattern.includes('MM')) {
    result = result.replace(/MM/g, (Array(2).join('0') + month).slice(-2));
  }
  if (pattern.includes('M')) {
    result = result.replace(/M/g, month);
  }
  if (pattern.includes('dd')) {
    result = result.replace(/dd/g, (Array(2).join('0') + day).slice(-2));
  }
  if (pattern.includes('d')) {
    result = result.replace(/d/g, day);
  }
  if (pattern.includes('HH')) {
    result = result.replace(/HH/g, (Array(2).join('0') + hour).slice(-2));
  }
  if (pattern.includes('H')) {
    result = result.replace(/H/g, hour);
  }
  if (pattern.includes('hh')) {
    result = result.replace(/hh/g, (Array(2).join('0') + (hour % 12)).slice(-2));
  }
  if (pattern.includes('h')) {
    result = result.replace(/h/g, hour % 12);
  }
  if (pattern.includes('mm')) {
    result = result.replace(/mm/g, (Array(2).join('0') + minute).slice(-2));
  }
  if (pattern.includes('m')) {
    result = result.replace(/m/g, minute);
  }
  if (pattern.includes('ss')) {
    result = result.replace(/ss/g, (Array(2).join('0') + second).slice(-2));
  }
  if (pattern.includes('s')) {
    result = result.replace(/s/g, second);
  }

  return result;
}

/**
 * 获取星期几
 */
export function getWeek() {
  const day = new Date().getDay();
  return `星期${'日一二三四五六'.charAt(day)}`;
}
