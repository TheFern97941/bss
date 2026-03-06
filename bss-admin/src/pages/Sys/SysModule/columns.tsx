import { ProColumns } from '@ant-design/pro-components';
import { defaultColumns } from '@/components/Field/Field';

const columns: ProColumns<API.SysModuleModel>[] = [
  {
    title: 'ID',
    dataIndex: 'id',
    valueType: 'text',
  },
  {
    title: '名称',
    dataIndex: 'name',
    copyable: true,
  },
  {
    title: '路径',
    dataIndex: 'path',
    copyable: true,
  },
  {
    title: '父路径',
    dataIndex: 'parent',
  },
  ...defaultColumns,
];

export default columns;
