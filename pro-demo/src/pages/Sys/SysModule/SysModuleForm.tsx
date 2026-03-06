import { MyDrawerForm } from '@/components/MyDrawerForm';
import {ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { postSysModule, postSysModuleCreate } from '@/services/backend/sysModuleManagerController';
import useAbortController from "@/hooks/useAbortController";
import {requestSysModuleToTreeSelect} from "@/pages/Sys/SysModule/service";
import {Form} from "antd";

const NAME = '系统模块';

const InitialValues = {};

interface SysRoleFormProps {
  entity?: API.SysModuleModel;
  onChange?: () => void;
  children?: React.ReactElement<any, any>;
}

export const SysModuleForm = (props: SysRoleFormProps) => {
  const { entity, onChange, children } = props;
  const isEdit = !!entity?.id;
  const actionName = isEdit ? '修改' : '添加';
  const abortSignal = useAbortController();

  const record = {
    ...entity,
  };

  return (
    <MyDrawerForm<any>
      title={`${actionName} ${NAME}`}
      onChange={onChange}
      record={record}
      initialValues={InitialValues}
      trigger={children}
      request={async (signal, params) => {
        if (isEdit) {
          await postSysModule({id: entity?.id}, params, {signal});
        } else {
          await postSysModuleCreate(params, {signal});
        }
      }}
    >
      <ProFormText name="id" label="id" disabled={isEdit} />
      <ProFormTreeSelect
        name="parent"
        label="parent"
        allowClear
        secondary
        fieldProps={{
          defaultOpen: true
        }}
        request={async () => {
          return requestSysModuleToTreeSelect(abortSignal)
        }}
      />
      <ProFormText name="name" label="name" required={true} />
      <ProFormText name="path" label="path" />
    </MyDrawerForm>
  );
};
