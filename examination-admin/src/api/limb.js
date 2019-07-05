import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/limb/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/limb/save',
  data,
  method: 'post',
});

export const remove = params => axios.request({
  url: '/limb/delete',
  params,
  method: 'get',
});

export const getListByCheckItem = params => axios.request({
  url: '/limb/getListByCheckItem',
  params,
  method: 'get',
});
