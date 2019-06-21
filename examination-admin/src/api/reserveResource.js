import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/reserveResource/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/reserveResource/save',
  data,
  method: 'post',
});

export const remove = params => axios.request({
  url: '/reserveResource/delete',
  params,
  method: 'get',
});

export const getTableList = params => axios.request({
  url: '/reserveResource/getTableList',
  params,
  method: 'get',
});

export const batchSave = data => axios.request({
  url: '/reserveResource/batchSave',
  data,
  method: 'post',
});
