import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/reserveTemplate/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/reserveTemplate/save',
  data,
  method: 'post',
});

export const remove = params => axios.request({
  url: '/reserveTemplate/delete',
  params,
  method: 'get',
});
