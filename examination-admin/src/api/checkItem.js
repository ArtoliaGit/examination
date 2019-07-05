import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/checkItem/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/checkItem/save',
  data,
  method: 'post',
});

export const remove = params => axios.request({
  url: '/checkItem/delete',
  params,
  method: 'get',
});

export const getAllList = params => axios.request({
  url: '/checkItem/getAllList',
  params,
  method: 'get',
});

export const getCheckItemByConditions = params => axios.request({
  url: '/checkItem/getCheckItemByConditions',
  params,
  method: 'get',
});
