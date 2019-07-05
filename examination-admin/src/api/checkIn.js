import axios from '@/utils/api.request';

export const getList = params => axios.request({
  url: '/checkIn/getList',
  params,
  method: 'get',
});

export const updateStatus = data => axios.request({
  url: '/checkIn/updateStatus',
  data,
  method: 'post',
});
