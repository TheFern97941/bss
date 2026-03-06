import { ModalForm, ProFormText } from '@ant-design/pro-components';
import { message } from 'antd';
import { useCallback, useState } from 'react';
import {postAccountPwd} from "@/services/backend/adminAccountController";

const useChangePassword = () => {
  const [modalVisit, setModalVisit] = useState(false);

  const openChangePasswordModal = useCallback(() => {
    setModalVisit(true);
  }, []);

  const rules = [
    {
      required: true,
      message: "请填入密码, 最小4位",
      min: 4,
    },
  ];

  const ChangePwdForm = () => {
    return (
      <ModalForm<{
        oldPassword: string,
        password: string,
        confirmPassword: string,
      }>
        title="修改密码"
        open={modalVisit}
        onOpenChange={setModalVisit}
        autoFocusFirstInput
        modalProps={{
          destroyOnClose: true,
          width: '400px',
        }}
        onFinish={async (values) => {
          if (values.confirmPassword !== values.password) {
            message.warning("新密码不匹配")
            return false;
          } else {
            await postAccountPwd(values);
            message.success('提交成功');
            return true;
          }
        }}
      >
        <ProFormText.Password
          rules={rules}
          width="md"
          name="oldPassword"
          label="旧密码"
          placeholder="旧密码"
        />
        <ProFormText.Password
          rules={rules}
          width="md"
          name="password"
          label="新密码"
          placeholder="新密码"
        />
        <ProFormText.Password
          rules={rules}
          width="md"
          name="confirmPassword"
          label="确认新密码"
          placeholder="确认新密码"
        />
      </ModalForm>
    );
  };

  return {
    openChangePasswordModal,
    ChangePwdForm,
  };
};

export default useChangePassword;
