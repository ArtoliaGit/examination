import routers from '@/route/routers';
import router from '@/route';
import
{
  getMenuByRouter,
  getBreadCrumbList,
  getHomeRoute,
  getTagNavListFromLocalstorage,
  setTagNavListInLocalstorage,
  routeEqual,
  getNextRoute,
  routeHasExist,
  getRouteTitleHandled,
  getRouterAccess,
} from '@/utils/utils';
import config from '@/config';

const { homeName } = config;

const closePage = (state, route) => {
  const nextRoute = getNextRoute(state.tagNavList, route);
  state.tagNavList = state.tagNavList.filter(item => !routeEqual(item, route));
  router.push(nextRoute);
};

export default {
  state: {
    breadCrumbList: [],
    tagNavList: [],
    homeRoute: {},
  },
  mutations: {
    setBreadCrumb(state, route) {
      state.breadCrumbList = getBreadCrumbList(route, state.homeRoute);
    },
    setHomeRoute(state, routes) {
      state.homeRoute = getHomeRoute(routes, homeName);
    },
    setTagNavList(state, list) {
      let tagList = [];
      // debugger;
      if (list) {
        tagList = [...list];
      } else {
        tagList = getTagNavListFromLocalstorage() || [];
        // tagList = [];
      }
      if (tagList[0] && tagList[0].name !== homeName) tagList.shift();
      const homeTagIndex = tagList.findIndex(item => item.name === homeName);
      if (homeTagIndex > 0) {
        const homeTag = tagList.splice(homeTagIndex, 1)[0];
        tagList.unshift(homeTag);
      }
      state.tagNavList = tagList;
      setTagNavListInLocalstorage([...tagList]);
    },
    closeTag(state, route) {
      const tag = state.tagNavList.filter(item => routeEqual(item, route));
      route = tag[0] ? tag[0] : null;
      if (!route) return;
      closePage(state, route);
    },
    addTag(state, { route, type = 'unshift' }) {
      const openRouter = getRouteTitleHandled(route);
      if (!routeHasExist(state.tagNavList, openRouter)) {
        if (type === 'push') state.tagNavList.push(openRouter);
        else if (openRouter.name === homeName) state.tagNavList.unshift(openRouter);
        else state.tagNavList.splice(1, 0, openRouter);
        setTagNavListInLocalstorage([...state.tagNavList]);
      }
    },
  },
  getters: {
    menuList: (state, getters, rootState) => {
      let accessRouters = routers;
      if (!rootState.user.access.includes(config.super_role)) {
        accessRouters = getRouterAccess(routers, rootState.user.resource, rootState.user.access[0]);
      }
      return getMenuByRouter(accessRouters, rootState.user.access);
    },
  },
  actions: {

  },
};
