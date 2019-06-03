import axios from '@/utils/api.request';

export const getOrganList = params => axios.request({
  url: '/organ/getOrganList',
  params,
  method: 'get',
});

export const save = (data, params) => axios.request({
  url: '/organ/save',
  data,
  params,
  method: 'post',
});

export const deleteOrgan = data => axios.request({
  url: '/organ/deleteOrgan',
  data,
  method: 'post',
});

export const getAllList = () => axios.request({
  url: '/organ/getAllList',
  method: 'get',
});
