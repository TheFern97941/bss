import {ProColumns} from "@ant-design/pro-components";
import {defaultColumns} from "@/components/Field/Field";

const columns: ProColumns<API.SysRole>[] = [
  {
    title: 'ID',
    dataIndex: 'id',
    valueType: 'text',
    ellipsis: true,
  },
  {
    title: '名称',
    dataIndex: 'name',
    copyable: true,
  },
  {
    title: "权限",
    dataIndex: "moduleId",
    hideInTable: true,
    hideInSearch: true,
  },
  ...defaultColumns,
];

export default columns;
