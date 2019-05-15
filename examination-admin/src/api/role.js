import axios from '@/utils/api.request';

export const getRoleList = params => axios.request({
  url: '/role/getRoleList',
  params,
  method: 'get',
});

export const save = (data, params) => axios.request({
  url: '/role/save',
  data,
  params,
  method: 'post',
});

export const deleteRole = params => axios.request({
  url: '/role/delete',
  params,
  method: 'get',
});

export const saveResource = data => axios.request({
  url: '/role/saveResource',
  data,
  method: 'post',
});
