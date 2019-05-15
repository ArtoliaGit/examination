import
{
  login,
  getUserInfo,
} from '@/api/login';
import
{
  getToken,
  setToken,
} from '@/utils/utils';
import router from '@/route';

export default {
  state: {
    userName: '',
    userId: '',
    avatorImgpath: '',
    token: getToken(),
    hasGetInfo: false,
    access: [],
    roleList: [],
    resource: [],
  },
  mutations: {
    SET_USERNAME(state, userName) {
      state.userName = userName;
    },
    SET_USERID(state, userId) {
      state.userId = userId;
    },
    SET_AVATOR(state, avatorImgpath) {
      state.avatorImgpath = avatorImgpath;
    },
    SET_TOKEN(state, token) {
      state.token = token;
      setToken(token);
    },
    SET_ACCESS(state, access) {
      state.access = access;
    },
    SET_HASGETINFO(state, status) {
      state.hasGetInfo = status;
    },
    SET_ROLELIST(state, roleList) {
      state.roleList = roleList;
    },
    SET_RESOURCE(state, resource) {
      state.resource = resource;
    },
  },
  getters: {

  },
  actions: {
    /**
     * @description 登陆
     */
    handleLogin({ commit }, { username, password }) {
      username = username.trim();
      return new Promise((resolve, reject) => {
        login({ username, password }).then((res) => {
          if (res.code === 200) {
            const { data } = res;
            commit('SET_TOKEN', data);
            resolve();
          } else {
            reject(res);
          }
        }).catch(error => reject(error));
      });
    },
    /**
     * @description 退出登陆
     */
    handleLogout({ commit }) {
      commit('SET_TOKEN', '');
      commit('SET_ACCESS', []);
      router.push({
        name: 'login',
      });
    },
    /**
     * @description 获取用户信息
     */
    getUserInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then((res) => {
          if (res.code === 200) {
            const { data } = res;
            commit('SET_USERNAME', data.username);
            commit('SET_USERID', data.userId);
            commit('SET_ROLELIST', data.roles);
            commit('SET_HASGETINFO', true);
            const access = data.roles.map(item => item.roleName);
            commit('SET_ACCESS', access);
            if (data.roles && data.roles[0].resource) {
              commit('SET_RESOURCE', data.roles[0].resource);
            }
            data.access = access;
            resolve(data);
          } else {
            reject(res);
          }
        }).catch(error => reject(error));
      });
    },
  },
};
