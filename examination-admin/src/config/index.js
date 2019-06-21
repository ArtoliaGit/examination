export default {
  /**
   * @description api请求基础路径
   */
  baseUrl: {
    dev: '/api',
    pro: '',
  },
  /**
   * @description token在Cookie中存储的天数，默认1天
   */
  cookieExpires: 1,
  /**
   * @description token
   */
  TOKEN_KEY: 'token',
  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home',
  /**
   * @description 配置显示在浏览器标签的title
   */
  title: 'examination-admin',
  /**
   * @description 超级管理员角色
   */
  super_role: 'ROLE_ADMIN',
};
