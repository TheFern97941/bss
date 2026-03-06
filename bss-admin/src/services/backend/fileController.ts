// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 POST /fileUpload */
export async function postFileUpload(options?: { [key: string]: any }) {
  return request<string>('/fileUpload', {
    method: 'POST',
    ...(options || {}),
  });
}
