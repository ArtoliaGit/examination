import Cookies from 'js-cookie';
import config from '@/config';
import { objEqual } from './tools';

const { cookieExpires, TOKEN_KEY, title } = config;

/**
 * @description 设置cookie中的token
 * @param {String} token
 */
export const setToken = (token) => {
  Cookies.set(TOKEN_KEY, token, { expires: cookieExpires || 1 });
};

/**
 * @description 从cookie中获取token
 */
export const getToken = () => {
  const token = Cookies.get(TOKEN_KEY);
  if (token) {
    return token;
  }
  return '';
};

/**
 * @param {String} url
 * @description 从URL中解析参数
 */
export const getUrlParams = (url) => {
  const keyValueArr = url.split('?')[1].split('&');
  const paramObj = {};
  keyValueArr.forEach((item) => {
    const keyValue = item.split('=');
    [paramObj[keyValue[0]]] = [keyValue[1]];
  });
  return paramObj;
};

const hasAccess = (access, route) => {
  if (route.meta && route.meta.access) {
    return access.some(item => route.meta.access.includes(item));
  }
  return true;
};

/**
 * @description 验证路由权限
 * @param {String} name 即将跳转的路由名
 * @param {Array} access 权限
 * @param {Array} routes 路由列表
 */
/* eslint array-callback-return: 0, no-else-return: 0 */
export const canTurnTo = (name, access, routes) => {
  const routePermissionJudge = list => list.some((item) => {
    if (item.children && item.children.length > 0) {
      return routePermissionJudge(item.children);
    } else if (item.name === name) {
      return hasAccess(access, item);
    }
    // return false;
  });
  return routePermissionJudge(routes);
};

/* eslint no-underscore-dangle: 0, consistent-return: 0 */
export const showTitle = (item) => {
  let mTitle = item.meta.title;
  if (!mTitle) {
    return;
  }
  mTitle = (item.meta && item.meta.title) || item.name;
  return mTitle;
};

export const getRouteTitleHandled = (route) => {
  const router = { ...route };
  const meta = { ...route.meta };
  let mTitle = '';
  if (meta.title) {
    if (typeof meta.title === 'function') {
      meta.__titleIsFunction__ = true;
      mTitle = meta.title(router);
    } else {
      mTitle = meta.title;
    }
  }
  meta.title = mTitle;
  router.meta = meta;
  return router;
};

/**
 * @description 根据当前跳转的路由设置显示在浏览器标签的title
 * @param {Object} routeItem 路由对象
 */
export const setTitle = (routeItem) => {
  const handledRoute = getRouteTitleHandled(routeItem);
  const pageTitle = showTitle(handledRoute);
  const resTitle = pageTitle ? `${title} - ${pageTitle}` : title;
  window.document.title = resTitle;
};

/**
 * 判断是否有子路由
 * @param {Object} item 路由
 */
export const hasChild = item => item.children && item.children.length !== 0;

/**
 * @description 通过路由列表得到菜单列表
 * @param {Array} routers 路由列表
 * @param {Array} access 权限
 */
export const getMenuByRouter = (routers, access) => {
  const res = [];
  routers.forEach((item) => {
    if (!item.meta || (item.meta && !item.meta.hideInMenu)) {
      const obj = {
        icon: (item.meta && item.meta.icon) || '',
        name: item.name,
        meta: item.meta,
      };
      if ((hasChild(item) || (item.meta && item.meta.showAlways))
        && hasAccess(access, item)) {
        obj.children = getMenuByRouter(item.children, access);
      }
      if (item.meta && item.meta.href) {
        obj.href = item.meta.href;
      }
      if (hasAccess(access, item)) {
        res.push(obj);
      }
    }
  });
  return res;
};

/**
 * @description 设置路由权限
 * @param {Array} routers 路由列表
 * @param {Array} resource 权限
 */
export const getRouterAccess = (routers, resource, role) => {
  const res = [];
  routers.forEach((item) => {
    if (!item.meta || (item.meta && !item.meta.hideInMenu)) {
      if (item.children && item.children.length > 0) {
        getRouterAccess(item.children, resource, role);
      }
      if (resource.includes(item.name)) {
        if (item.meta.access && !item.meta.access.includes(role)) {
          item.meta.access.push(role);
        } else if (!item.meta.access || item.meta.access.length === 0) {
          item.meta.access = [role];
        }
      }
      if (!item.meta.access) item.meta.access = [];
    }
    res.push(item);
  });
  return res;
};

/**
 * @param {Array} routeMetched 当前路由metched
 * @returns {Array}
 */
export const getBreadCrumbList = (route, homeRoute) => {
  const homeItem = { ...homeRoute, icon: homeRoute.meta.icon };
  const routeMetched = route.matched;
  if (routeMetched.some(item => item.name === homeRoute.name)) return [homeItem];
  let res = routeMetched.filter(item => item.meta === undefined
    || !item.meta.hideInBread).map((item) => {
    const meta = { ...item.meta };
    if (meta.title && typeof meta.title === 'function') {
      meta.__titleIsFunction__ = true;
      meta.title = meta.title(route);
    }
    const obj = {
      icon: (item.meta && item.meta.icon) || '',
      name: item.name,
      meta,
    };
    return obj;
  });
  res = res.filter(item => !item.meta.hideInMenu);
  return [{ ...homeItem, to: { path: homeRoute.path } }, ...res];
};

/**
 * @param {Array} routers 路由列表数组
 * @description 用于找到路由列表中name为home的对象
 */
/* eslint no-plusplus: 0 */
export const getHomeRoute = (routers, homeName = 'home') => {
  let i = -1;
  const len = routers.length;
  let homeRoute = {};
  while (++i < len) {
    const item = routers[i];
    if (item.children && item.children.length) {
      const res = getHomeRoute(item.children, homeName);
      if (res.name) return res;
    } else if (item.name === homeName) homeRoute = item;
  }
  return homeRoute;
};

/**
 * @description 根据name/params/query判断两个路由对象是否相等
 * @param {*} route1 路由对象
 * @param {*} route2 路由对象
 */
export const routeEqual = (route1, route2) => {
  const params1 = route1.params || {};
  const params2 = route2.params || {};
  const query1 = route1.query || {};
  const query2 = route2.query || {};
  return (route1.name === route2.name) && objEqual(params1, params2) && objEqual(query1, query2);
};

/**
 * @description 本地存储和获取标签导航列表
 */
export const setTagNavListInLocalstorage = (list) => {
  localStorage.tagNaveList = JSON.stringify(list);
};

/**
 * @returns {Array} 其中的每个元素只包含路由原信息中的name, path, meta三项
 */
export const getTagNavListFromLocalstorage = () => {
  const list = localStorage.tagNaveList;
  return list ? JSON.parse(list) : [];
};

/**
 * @param {*} list 现有标签导航列表
 * @param {*} newRoute 新添加的路由原信息对象
 * @description 如果该newRoute已经存在则不再添加
 */
export const getNewTagList = (list, newRoute) => {
  const { name, path, meta } = newRoute;
  const newList = [...list];
  if (newList.findIndex(item => item.name === name) >= 0) return newList;
  newList.push({ name, path, meta });
  return newList;
};

/**
 * @param {Array} list 标签列表
 * @param {String} name 当前关闭的标签的name
 */
export const getNextRoute = (list, route) => {
  let res = {};
  if (list.length === 2) {
    res = getHomeRoute(list);
  } else {
    const index = list.findIndex(item => routeEqual(item, route));
    if (index === list.length - 1) res = list[list.length - 2];
    else res = list[index + 1];
  }
  return res;
};

/**
 * @param {Number} times 回调函数需要执行的次数
 * @param {Function} callback 回调函数
 */
export const doCustomTimes = (times, callback) => {
  let i = -1;
  while (++i < times) {
    callback(i);
  }
};

/**
 * 判断打开的标签列表里是否已存在这个新添加的路由对象
 */
export const routeHasExist = (tagNavList, routeItem) => {
  const len = tagNavList.length;
  let res = false;
  doCustomTimes(len, (index) => {
    if (routeEqual(tagNavList[index], routeItem)) res = true;
  });
  return res;
};
