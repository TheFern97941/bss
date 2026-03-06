import { Command } from '@/components/MyProTable/components/Command';
import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import { SysModuleForm } from './SysModuleForm';
import { deleteSysRole } from '@/services/backend/sysRoleManagerController';

const commands: Command<API.SysModuleModel>[] = [
  {
    label: '添加',
    icon: <PlusOutlined />,
    confirm: false,
    row: false,
    type: "primary",
    children: (record, action) => {
      return <SysModuleForm entity={record} onChange={() => action?.reload()} />;
    },
  },
  // {
  //   label: '查看',
  //   icon: <EyeOutlined />,
  //   confirm: false,
  //   row: true,
  //   tool: false,
  //   onHandle: async (row) => {
  //     console.log('del: ', row);
  //   },
  // },
  {
    label: '编辑',
    icon: <EditOutlined />,
    confirm: false,
    tool: false,
    row: true,
    children: (record, action) => {
      return <SysModuleForm entity={record} onChange={() => action?.reload()} />;
    },
  },
  {
    label: '删除',
    icon: <DeleteOutlined />,
    confirm: true,
    row: true,
    tool: false,
    danger: true,
    onHandle: async (row) => {
      if (row?.id) {
        await deleteSysRole({ id: row.id });
      }
    },
  },
];

export default commands;
