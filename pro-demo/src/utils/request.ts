import { Result } from '@/requestErrorConfig';
import { request as umiRequest } from '@umijs/max';
import type { SortOrder } from 'antd/lib/table/interface';

const urlPrefix = '/api';

function toBackendSortOrder(sortOrder: SortOrder): string {
  let res = 'DESC';
  switch (sortOrder) {
    case 'descend':
      res = 'DESC';
      break;
    case 'ascend':
      res = 'ASC';
      break;
  }
  return res;
}

function convertSortOrderToString(sort: Record<string, SortOrder>): Record<string, string> {
  return Object.fromEntries(
    Object.entries(sort)
      .filter(([, value]) => value)
      .map(([key, value]) => {
        const tv = toBackendSortOrder(value);
        return [key, tv];
      }),
  );
}

export function toPageQuery<U>(
  params: U & {
    pageSize?: number;
    current?: number;
    keyword?: string;
  },
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
) {
  const { pageSize, current, ...restParams } = params;
  const transferSort = convertSortOrderToString(sort);
  const filtered = Object.fromEntries(
    Object.entries(restParams).filter(([_, v]) => v !== '')
  );
  return {
    current: current ? current - 1 : 0,
    pageSize: pageSize ?? 25,
    sort: transferSort,
    filter: filtered,
  };
}

export async function request<T>(url: string, options: { [key: string]: any }) {
  const res = await umiRequest<Result<T>>(`${urlPrefix}${url}`, options);
  return res.data;
}
