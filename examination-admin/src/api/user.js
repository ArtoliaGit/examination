import axios from '@/utils/api.request';

export const getUserList = params => axios.request({
  url: '/user/getUserList',
  params,
  method: 'get',
});

export const save = (data, params) => axios.request({
  url: '/user/save',
  data,
  params,
  method: 'post',
});

export const deleteUser = params => axios.request({
  url: '/user/delete',
  params,
  method: 'get',
});
