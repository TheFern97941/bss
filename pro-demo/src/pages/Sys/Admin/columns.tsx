import {ProColumns} from "@ant-design/pro-components";
import {Space, Tag} from "antd";
import {defaultColumns} from "@/components/Field/Field";
import {requestSysRoleToSelect} from "../SysRole/service";
import {objectIdField} from "@/components/Field/objectId/ObjectIdField";



const columns: ProColumns<API.Admin>[] = [
  objectIdField(),
  {
    title: '昵称',
    dataIndex: 'name',
    copyable: true,
  },
  {
    title: '标题',
    dataIndex: 'title',
    hideInSearch: true,
  },
  {
    title: '头像',
    dataIndex: 'avatar',
    valueType: 'avatar',
    hideInSearch: true,
  },
  {
    title: "角色",
    dataIndex: 'role',
    valueType: 'select',
    request: requestSysRoleToSelect,
  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,
    ellipsis: true,
  },
  {
    title: '锁定',
    dataIndex: 'lock',
    hideInSearch: true,
    disable: true,
    render: (_, record) => (
      <Space>
        <Tag color={record.lock > 7 ? 'red' : 'default'}>
          {record.lock > 7 ? '锁定' : '未锁定'}({record.lock})
        </Tag>
      </Space>
    ),
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    hideInSearch: true,
  },
  ...defaultColumns,
];

export default columns;
