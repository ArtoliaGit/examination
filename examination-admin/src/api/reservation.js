import axios from '@/utils/api.request';

export const getReserveCalendar = params => axios.request({
  url: '/reserve/getReserveCalendar',
  params,
  method: 'get',
});

export const getTimeSlot = params => axios.request({
  url: '/reserve/getTimeSlot',
  params,
  method: 'get',
});

export const getReservePersonInfo = params => axios.request({
  url: '/reserve/getReservePersonInfo',
  params,
  method: 'get',
});

export const getList = params => axios.request({
  url: '/reserve/getList',
  params,
  method: 'get',
});

export const save = data => axios.request({
  url: '/reserve/save',
  data,
  method: 'post',
});

export const cancelReserve = params => axios.request({
  url: '/reserve/cancelReserve',
  params,
  method: 'get',
});
