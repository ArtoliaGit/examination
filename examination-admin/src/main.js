import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/icon-font/iconfont.css';
import App from './App.vue';
import router from './route';
import store from './store';
import config from './config';
import Print from './utils/print';

Vue.use(ElementUI);
Vue.use(Print);

Vue.config.productionTip = false;

/**
 * @description 全局注册应用配置
 */
Vue.prototype.$config = config;

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
