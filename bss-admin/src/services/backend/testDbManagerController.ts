// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /testDb/${param0} */
export async function getTestDb(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTestDbParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.TestDb>(`/testDb/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /testDb/${param0} */
export async function postTestDb(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.postTestDbParams,
  body: API.TestDbForm,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.TestDb>(`/testDb/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /testDb/${param0} */
export async function deleteTestDb(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteTestDbParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.TestDb>(`/testDb/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /testDb/all/${param0} */
export async function getTestDbAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTestDbAllParams,
  options?: { [key: string]: any },
) {
  const { ids: param0, ...queryParams } = params;
  return request<API.TestDb[]>(`/testDb/all/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /testDb/create */
export async function postTestDbCreate(body: API.TestDbForm, options?: { [key: string]: any }) {
  return request<API.TestDb>('/testDb/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /testDb/deleteAll/${param0} */
export async function deleteTestDbDeleteAll(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteTestDbDeleteAllParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.TestDb[]>(`/testDb/deleteAll/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /testDb/page */
export async function postTestDbPage(body: API.PageQuery, options?: { [key: string]: any }) {
  return request<API.PageTestDb>('/testDb/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
