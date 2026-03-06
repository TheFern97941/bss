import MyProTable from '@/components/MyProTable';
import { postSysAdminPage } from '@/services/backend/sysAdminManagerController';
import { toPageQuery } from '@/utils/request';
import { ActionType } from '@ant-design/pro-components';
import { useRef } from 'react';
import columns from "./columns";
import commands from "./commands";

export default () => {
  const actionRef = useRef<ActionType>();

  return (
    <MyProTable<API.Admin>
      headerTitle="系统管理员"
      request={async (params, sort, filter) => {
        console.log('request: ', params, sort, filter);
        return postSysAdminPage(toPageQuery(params, sort, filter));
      }}
      columns={columns}
      commands={commands}
    />
  );
};
