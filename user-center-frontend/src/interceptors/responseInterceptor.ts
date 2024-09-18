/**
 * request 网络请求工具
 * 更详细的 api 文档: https://github.com/umijs/umi-request
 */
import { history } from '@@/core/history';
import { message } from 'antd';
import { stringify } from 'querystring';
import { extend } from 'umi-request';

/**
 * 配置request请求时的默认参数
 */
const request = extend({
  credentials: 'include', // 默认请求是否带上cookie
  prefix: process.env.NODE_ENV === 'production' ? 'http://user-backend.us' : undefined,
  // requestType: 'form',
});

/**
 * 所有请求拦截器
 * https://github.com/umijs/umi-request/blob/master/README_zh-CN.md#%E6%8B%A6%E6%88%AA%E5%99%A8
 */
request.interceptors.request.use((url, options): any => {
  console.log(`do request url = ${url}`);

  return {
    url,
    options: {
      ...options,
    },
  };
});

/**
 * 所有响应拦截器
 */
request.interceptors.response.use(async (response, options): Promise<any> => {
  const res = await response.clone().json();
  if (res.code === 0) {
    return res.data;
  } else if (res.code === 40100) {
    message.error('Please login first');
    history.replace({
      pathname: '/user/login',
      search: stringify({
        redirect: location.pathname,
      }),
    });
  } else {
    message.error(res.description);
  }
  return res.data;
});

export { request };
