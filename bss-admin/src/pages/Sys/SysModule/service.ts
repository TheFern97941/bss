import {postSysModuleTreeSelect} from "@/services/backend/sysModuleManagerController";

export type SysModuleTreeModel = {
  title: string;
  value: string;
  children?: SysModuleTreeModel[];
};

function convertToTreeModel(modules: API.SysModuleModel[]): SysModuleTreeModel[] {
  return modules.map((item) => ({
    title: item.name || '',
    value: item.id,
    children: item.children ? convertToTreeModel(item.children) : undefined,
  }));
}

export async function requestSysModuleToTreeSelect(abortSignal: AbortSignal) {
  const pageSysModule  = await postSysModuleTreeSelect({abortSignal});
  const d = convertToTreeModel(pageSysModule.data)
  console.log("d: ", d);
  return d;
}
