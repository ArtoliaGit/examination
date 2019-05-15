import axios from '@/utils/api.request';

export const login = ({ username, password }) => {
  const data = {
    username,
    password,
  };
  return axios.request({
    url: 'authentication/login',
    data,
    method: 'post',
  });
};

export const getUserInfo = params => axios.request({
  url: 'user/getUserInfo',
  params,
  method: 'get',
});

export const logout = () => axios.request({
  url: 'logout',
  method: 'post',
});
