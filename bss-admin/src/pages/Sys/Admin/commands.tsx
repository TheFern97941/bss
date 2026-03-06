import { Command } from '@/components/MyProTable/components/Command';
import {
  DeleteOutlined,
  EditOutlined,
  LockOutlined,
  PlusOutlined,
  SecurityScanOutlined,
} from '@ant-design/icons';
import { AdminForm } from '@/pages/Sys/Admin/AdminForm';
import {
  deleteSysAdmin,
  postSysAdminUnbindAuthKey,
  postSysAdminUnlock,
} from '@/services/backend/sysAdminManagerController';

const commands: Command<API.Admin>[] = [
  {
    label: '添加',
    icon: <PlusOutlined />,
    confirm: false,
    row: false,
    children: (record, action) => {
      return <AdminForm entity={record} onChange={() => action?.reload()} />;
    },
  },
  {
    label: '编辑',
    icon: <EditOutlined />,
    confirm: false,
    tool: false,
    row: true,
    children: (record, action) => {
      return <AdminForm entity={record} onChange={() => action?.reload()} />;
    },
  },
  {
    label: '解锁',
    icon: <LockOutlined />,
    confirm: false,
    tool: false,
    row: true,
    isEnable: (record) => {
      return record.lock > 7;
    },
    onHandle: async (record) => {
      await postSysAdminUnlock({ id: record!.id });
    },
  },
  {
    label: '解除Google auth',
    icon: <SecurityScanOutlined />,
    confirm: true,
    tool: false,
    row: true,
    isEnable: (record) => {
      return !!record.authKey;
    },
    onHandle: async (record) => {
      await postSysAdminUnbindAuthKey({ id: record!.id });
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
        await deleteSysAdmin({ id: row.id });
      }
    },
  },
];

export default commands;
