// @ts-ignore
/* eslint-disable */

// import { request } from '@umijs/max';
import {request} from '@/interceptors/responseInterceptor';

/** 获取当前的用户 GET /api/user/current */
export async function currentUser(options?: { [key: string]: any }) {
  return request<API.BaseResponse<API.CurrentUser>>('/api/user/current', {
    method: 'GET',
    withCredentials: true,
    ...(options || {}),
  });
}

/** 退出登录接口 POST /api/user/logout */
export async function outLogin(options?: { [key: string]: any }) {
  return request<API.BaseResponse<number>>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 登录接口 POST /api/user/login */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<API.BaseResponse<API.LoginResult>>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 注册接口 POST /api/user/register */
export async function register(body: API.RegisterParams, options?: { [key: string]: any }) {
  return request<API.BaseResponse<API.RegisterResult>>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 搜索用户 GET /api/user/search */
export async function searchUsers(
  params?: { [key: string]: any },
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse<API.CurrentUser[]>>('/api/user/search', {
    method: 'GET',
    params: params,
    ...(options || {}),
  });
}

/** 根据 id 获取用户 GET /api/user?id=* */
export async function getUserById(
  params?: { id: number },
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse<API.CurrentUser[]>>('/api/user/', {
    method: 'GET',
    params: params,
    ...(options || {}),
  });
}

/** 更新用户 POST /api/user/update */
export async function updateUser(body: API.UserUpdateParams, options?: { [key: string]: any }) {
  return request<API.BaseResponse<boolean>>('/api/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 创建用户 POST /api/user/create?userAccount=* */
export async function createUser(
  params: { userAccount: string, userRole: number },
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse<number>>('/api/user/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 删除用户 POST /api/user/delete?id=* */
export async function deleteUser(
  params: { id: number },
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse<number>>('/api/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 搜索教学计划 GET /api/plan/search */
export async function searchPlans(
  params?: { [key: string]: any },
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse<API.Plan[]>>('/api/plan/search', {
    method: 'GET',
    params: params,
    ...(options || {}),
  });
}

/** 创建教学计划 POST /api/plan/create* */
export async function createPlan(body: API.PlanCreateParams, options?: { [key: string]: any }) {
  return request<API.BaseResponse<boolean>>('/api/plan/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新教学计划 POST /api/plan/create* */
export async function updatePlan(body: API.PlanUpdateParams, options?: { [key: string]: any }) {
  return request<API.BaseResponse<boolean>>('/api/plan/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除用户 POST /api/plan/delete?id=* */
export async function deletePlan(
  params: { id: number },
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse<boolean>>('/api/plan/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
