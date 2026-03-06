import MyProTable from '@/components/MyProTable';
import { toPageQuery } from '@/utils/request';
import { ActionType } from '@ant-design/pro-components';
import { useRef } from 'react';
import columns from "./columns";
import commands from "./commands";
import {postSysRolePage} from "@/services/backend/sysRoleManagerController";

export default () => {
  const actionRef = useRef<ActionType>();

  return (
    <MyProTable<API.SysRole>
      headerTitle="系统角色"
      request={async (params, sort, filter) => {
        console.log('request: ', params, sort, filter);
        return postSysRolePage(toPageQuery(params, sort, filter));
      }}
      columns={columns}
      commands={commands}
    />
  );
};
