import {postSysRolePage} from "@/services/backend/sysRoleManagerController";

export async function requestSysRoleToSelect() {
  const pageSysRole  = await postSysRolePage({current: 0, pageSize: 500, filter: {}});
  return pageSysRole?.data?.map(d => {
    return ({
      label: d.name,
      value: d.id,
    })
  });
}
