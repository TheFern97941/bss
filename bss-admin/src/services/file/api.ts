// @ts-ignore
/* eslint-disable */
import { Result } from '@/requestErrorConfig';
import { request } from '@umijs/max';
import UploadSignFileResult = API.UploadSignFileResult;

/** 获取当前的用户 GET /api/currentUser */
export async function currentUser(options?: { [key: string]: any }) {
  return await request<Result<UploadSignFileResult>>('/api/fileUpload', {
    method: 'POST',
    ...(options || {}),
  });
}
