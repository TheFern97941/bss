// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /sysModule/${param0} */
export async function getSysModule(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysModuleParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysModuleModel>(`/sysModule/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysModule/${param0} */
export async function postSysModule(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.postSysModuleParams,
  body: API.SysModuleForm,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysModuleModel>(`/sysModule/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /sysModule/${param0} */
export async function deleteSysModule(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysModuleParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysModuleModel>(`/sysModule/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /sysModule/all/${param0} */
export async function getSysModuleAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysModuleAllParams,
  options?: { [key: string]: any },
) {
  const { ids: param0, ...queryParams } = params;
  return request<API.SysModuleModel[]>(`/sysModule/all/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysModule/create */
export async function postSysModuleCreate(
  body: API.SysModuleForm,
  options?: { [key: string]: any },
) {
  return request<API.SysModuleModel>('/sysModule/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /sysModule/deleteAll/${param0} */
export async function deleteSysModuleDeleteAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSysModuleDeleteAllParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.SysModuleModel[]>(`/sysModule/deleteAll/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysModule/page */
export async function postSysModulePage(body: API.PageQuery, options?: { [key: string]: any }) {
  return request<API.PageSysModuleModel>('/sysModule/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysModule/tree */
export async function postSysModuleTree(options?: { [key: string]: any }) {
  return request<API.PageSysModuleModel>('/sysModule/tree', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysModule/treeSelect */
export async function postSysModuleTreeSelect(options?: { [key: string]: any }) {
  return request<API.PageSysModuleModel>('/sysModule/treeSelect', {
    method: 'POST',
    ...(options || {}),
  });
}
