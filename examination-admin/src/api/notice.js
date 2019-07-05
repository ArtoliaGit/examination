import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/notice/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/notice/save',
  data,
  method: 'post',
});

export const deleteNotice = params => axios.request({
  url: '/notice/delete',
  params,
  method: 'get',
});

export const list = params => axios.request({
  url: '/notice/list',
  params,
  method: 'get',
});
