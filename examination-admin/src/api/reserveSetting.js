import axios from '@/utils/api.request';

export const getData = () => axios.request({
  url: '/reserveSetting/getData',
  method: 'get',
});

export const save = data => axios.request({
  url: '/reserveSetting/save',
  data,
  method: 'post',
});
