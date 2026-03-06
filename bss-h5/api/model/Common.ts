
type SortDir = 'ASC' | 'DESC';

export interface PageQuery<T> {
  current: number;
  pageSize: number;
  sort?: Record<string, SortDir>;
  filter?: T;
}