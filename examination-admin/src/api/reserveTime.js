import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/reserveTime/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/reserveTime/save',
  data,
  method: 'post',
});

export const remove = params => axios.request({
  url: '/reserveTime/delete',
  params,
  method: 'get',
});

export const getAllList = params => axios.request({
  url: '/reserveTime/getAllList',
  params,
  method: 'get',
});
