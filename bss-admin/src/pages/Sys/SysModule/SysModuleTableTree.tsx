import {postSysModuleTree} from "@/services/backend/sysModuleManagerController";
import columns from "@/pages/Sys/SysModule/columns";
import commands from "@/pages/Sys/SysModule/commands";
import MyProTable from "@/components/MyProTable";
import useAbortController from "@/hooks/useAbortController";
import type {ProTableProps} from "@ant-design/pro-components";

interface SysModuleTableTreeProps {
  selectedKeys?: string[];
  onChangeSelectedKeys?: (selectKeys: string[]) => void;
  search?: ProTableProps<any, any>['search'];
}

const SysModuleTableTree = ({selectedKeys, onChangeSelectedKeys, search}: SysModuleTableTreeProps) => {
  const abortSignal = useAbortController();

  return (
    <MyProTable<API.SysModuleModel>
      headerTitle="系统模块"
      request={async () => {
        return await postSysModuleTree({ abortSignal });
      }}
      search={search}
      rowSelection={
        (!!onChangeSelectedKeys ? {
          selectedRowKeys: selectedKeys || [],
          onChange: (selectedRowKeys: any[]) => onChangeSelectedKeys(selectedRowKeys),
        } : {})
      }
      expandable={{
        defaultExpandAllRows: true,
      }}
      pagination={false}
      columns={columns}
      commands={commands}
    />
  )
}

export default SysModuleTableTree
