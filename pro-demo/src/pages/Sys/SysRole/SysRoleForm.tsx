import { MyDrawerForm } from '@/components/MyDrawerForm';
import { ProForm, ProFormGroup, ProFormText } from '@ant-design/pro-components';
import { postSysRole, postSysRoleCreate } from '@/services/backend/sysRoleManagerController';
import SysModuleTableTree from "@/pages/Sys/SysModule/SysModuleTableTree";
import {Form} from "antd";
import { useMemo } from 'react';

const NAME = '系统角色';

const InitialValues = {};

interface SysRoleFormProps {
  entity?: API.SysRole;
  onChange?: () => void;
  children?: React.ReactElement<any, any>;
}

const SysModuleFormWrapper = () => {

  const form = ProForm.useFormInstance();
  const selectedKeys = Form.useWatch?.('moduleId', form) || [];

  const com = useMemo(() => {
    console.log("default entity: ", form.getFieldsValue());
    console.log("default keys: ", selectedKeys);
    return (
      <SysModuleTableTree
        search={false}
        selectedKeys={selectedKeys}
        onChangeSelectedKeys={(newSelectedKeys) => {
          console.log("onChangeSelectedKeys: ", newSelectedKeys);
          form.setFieldValue('moduleId', newSelectedKeys);
        }}
      />
    )
  }, [selectedKeys])

  return (
    com
  );
};

export const SysRoleForm = (props: SysRoleFormProps) => {
  const { entity, onChange, children } = props;
  const isEdit = !!entity?.id;
  const actionName = isEdit ? '修改' : '添加';

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
          await postSysRole({id: entity?.id}, params, {signal});
        } else {
          await postSysRoleCreate(params, {signal});
        }
      }}
    >
      <ProFormText name="id" label="id" disabled={isEdit} />
      <ProFormText name="name" label="name" />
      <ProFormGroup>
        <ProForm.Item noStyle name="moduleId">
          <SysModuleFormWrapper />
        </ProForm.Item>
      </ProFormGroup>
    </MyDrawerForm>
  );
};


