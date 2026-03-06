// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /account */
export async function getAccount(options?: { [key: string]: any }) {
  return request<API.AdminModel>('/account', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /account */
export async function postAccount(body: API.UpdateAdminForm, options?: { [key: string]: any }) {
  return request<API.AdminModel>('/account', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /account/bindMyGoogleAuth */
export async function postAccountBindMyGoogleAuth(
  body: API.BindGoogleAuthForm,
  options?: { [key: string]: any },
) {
  return request<boolean>('/account/bindMyGoogleAuth', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /account/login */
export async function postAccountLogin(body: API.LoginForm, options?: { [key: string]: any }) {
  return request<API.LoginLog>('/account/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /account/logout */
export async function postAccountLogout(options?: { [key: string]: any }) {
  return request<API.Unit>('/account/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /account/pwd */
export async function postAccountPwd(body: API.ChangePwdForm, options?: { [key: string]: any }) {
  return request<API.Admin>('/account/pwd', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /account/startGoogleAuth */
export async function postAccountStartGoogleAuth(options?: { [key: string]: any }) {
  return request<string>('/account/startGoogleAuth', {
    method: 'POST',
    ...(options || {}),
  });
}
