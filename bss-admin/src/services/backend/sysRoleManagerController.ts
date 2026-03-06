// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /sysRole/${param0} */
export async function getSysRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysRoleParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysRole>(`/sysRole/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysRole/${param0} */
export async function postSysRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.postSysRoleParams,
  body: API.SysRoleForm,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysRole>(`/sysRole/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /sysRole/${param0} */
export async function deleteSysRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysRoleParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysRole>(`/sysRole/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /sysRole/all/${param0} */
export async function getSysRoleAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysRoleAllParams,
  options?: { [key: string]: any },
) {
  const { ids: param0, ...queryParams } = params;
  return request<API.SysRole[]>(`/sysRole/all/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysRole/create */
export async function postSysRoleCreate(body: API.SysRoleForm, options?: { [key: string]: any }) {
  return request<API.SysRole>('/sysRole/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /sysRole/deleteAll/${param0} */
export async function deleteSysRoleDeleteAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysRoleDeleteAllParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysRole[]>(`/sysRole/deleteAll/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysRole/page */
export async function postSysRolePage(body: API.PageQuery, options?: { [key: string]: any }) {
  return request<API.PageSysRole>('/sysRole/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
