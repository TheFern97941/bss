// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /sysAdmin/${param0} */
export async function getSysAdmin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysAdminParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Admin>(`/sysAdmin/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysAdmin/${param0} */
export async function postSysAdmin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.postSysAdminParams,
  body: API.AdminForm,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Admin>(`/sysAdmin/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /sysAdmin/${param0} */
export async function deleteSysAdmin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysAdminParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Admin>(`/sysAdmin/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /sysAdmin/all/${param0} */
export async function getSysAdminAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysAdminAllParams,
  options?: { [key: string]: any },
) {
  const { ids: param0, ...queryParams } = params;
  return request<API.Admin[]>(`/sysAdmin/all/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysAdmin/create */
export async function postSysAdminCreate(body: API.AdminForm, options?: { [key: string]: any }) {
  return request<API.Admin>('/sysAdmin/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /sysAdmin/deleteAll/${param0} */
export async function deleteSysAdminDeleteAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysAdminDeleteAllParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Admin[]>(`/sysAdmin/deleteAll/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysAdmin/page */
export async function postSysAdminPage(body: API.PageQuery, options?: { [key: string]: any }) {
  return request<API.PageAdmin>('/sysAdmin/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysAdmin/unbindAuthKey/${param0} */
export async function postSysAdminUnbindAuthKey(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.postSysAdminUnbindAuthKeyParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Admin>(`/sysAdmin/unbindAuthKey/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysAdmin/unlock/${param0} */
export async function postSysAdminUnlock(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.postSysAdminUnlockParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.Admin>(`/sysAdmin/unlock/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}
