// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /test/hello */
export async function getTestHello(options?: { [key: string]: any }) {
  return request<string>('/test/hello', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PUT /test/hello */
export async function putTestHello(options?: { [key: string]: any }) {
  return request<string>('/test/hello', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /test/hello */
export async function postTestHello(options?: { [key: string]: any }) {
  return request<string>('/test/hello', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /test/hello */
export async function deleteTestHello(options?: { [key: string]: any }) {
  return request<string>('/test/hello', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PATCH /test/hello */
export async function patchTestHello(options?: { [key: string]: any }) {
  return request<string>('/test/hello', {
    method: 'PATCH',
    ...(options || {}),
  });
}
