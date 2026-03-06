import { MyDrawerForm } from '@/components/MyDrawerForm';
import MySingleImageProFormUploadButton, {
  convertImageToUploadFormat,
} from '@/components/MySingleImageProFormUploadButton';
import { requestSysRoleToSelect } from '@/pages/Sys/SysRole/service';
import { postSysAdmin, postSysAdminCreate } from '@/services/backend/sysAdminManagerController';
import { StateEnum } from '@/services/typings';
import { ProFormSelect, ProFormText } from '@ant-design/pro-components';

const NAME = '管理员账号';

const InitialValues = {};

interface AdminFormProps {
  entity?: API.Admin;
  onChange?: () => void;
  children?: React.ReactElement<any, any>;
}

export const AdminForm = (props: AdminFormProps) => {
  const { entity, onChange, children } = props;
  const isEdit = !!entity?.id;
  const actionName = isEdit ? '修改' : '添加';

  const record = {
    ...entity,
    password: undefined,
    avatar: convertImageToUploadFormat(entity?.avatar, '头像'),
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
          await postSysAdmin({ id: entity?.id }, params, { signal });
        } else {
          await postSysAdminCreate(params, { signal });
        }
      }}
    >
      <ProFormText name="name" label="name" />
      <ProFormText name="title" label="title" />
      <ProFormText name="username" label="username" />
      <ProFormText name="password" label="password" />
      <ProFormSelect
        name="status"
        label="status"
        valueEnum={StateEnum}
        initialValue={StateEnum.NORMAL}
      />
      <ProFormSelect name="role" label="role" request={requestSysRoleToSelect} />
      <ProFormText name="email" label="email" />
      <MySingleImageProFormUploadButton name="avatar" label="头像" />
    </MyDrawerForm>
  );
};
