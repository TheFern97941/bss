// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/request';

/** 此处后端没有提供注释 GET /sysSetting/${param0} */
export async function getSysSetting(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSysSettingParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.AllSetting>(`/sysSetting/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /sysSetting/authSetting */
export async function postSysSettingAuthSetting(
  body: API.AuthSetting,
  options?: { [key: string]: any },
) {
  return request<API.AllSetting>('/sysSetting/authSetting', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
