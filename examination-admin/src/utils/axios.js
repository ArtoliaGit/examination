import axios from 'axios';
import { Message } from 'element-ui';
import store from '@/store';
import router from '@/route';

/**
 * @description 错误日志
 * @param {Object} errorInfo
 */
const addErrorLog = (errorInfo) => {
  const { statusText, status, request: { responseURL } } = errorInfo;
  const info = {
    type: 'ajax',
    code: status,
    message: statusText,
    url: responseURL,
  };
  console.log(info);
};

class HttpRequest {
  constructor(baseUrl = '') {
    this.baseUrl = baseUrl;
    this.queue = {};
  }

  getInsiderConfig() {
    const config = {
      baseURL: this.baseUrl,
      timeout: 60000,
      headers: {
        //
      },
    };
    return config;
  }

  destroy(url) {
    delete this.queue[url];
    if (!Object.keys(this.queue).length) {
      //
    }
  }

  interceptors(instance, url) {
    /**
     * @description 请求拦截
     */
    instance.interceptors.request.use((config) => {
      if (!Object.keys(this.queue).length) {
        //
      }
      this.queue[url] = true;

      if (store.state.user.token) {
        config.headers.Authorization = store.state.user.token;
      }

      return config;
    }, error => Promise.reject(error));

    /**
     * @description 响应拦截
     */
    instance.interceptors.response.use((res) => {
      this.destroy(url);
      const { data } = res;
      return data;
    }, (error) => {
      this.destroy(url);
      let errorInfo = error.response;
      if (!errorInfo) {
        const { request: { statusText, status }, config } = JSON.parse(JSON.stringify(error));
        errorInfo = {
          statusText,
          status,
          request: { responseURL: config.url },
        };
      }
      if (errorInfo.status === 504 || errorInfo.status === 404) {
        Message({
          type: 'error',
          message: '网络异常',
        });
      } else if (errorInfo.status === 403) {
        if (router.currentRoute.path !== '/login') {
          Message({
            type: 'error',
            message: '请重新登录',
          });
          store.dispatch('handleLogout');
          router.push({
            name: 'login',
          });
        }
      }
      addErrorLog(errorInfo);
      return Promise.reject(error);
    });
  }

  request(options) {
    const instance = axios.create();
    const mergeOptions = Object.assign(this.getInsiderConfig(), options);
    this.interceptors(instance, mergeOptions.url);
    return instance(mergeOptions);
  }
}

export default HttpRequest;
