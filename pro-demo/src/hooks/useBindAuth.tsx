import { useFetchUserInfo } from '@/hooks/useFetchUserInfo';
import { CopyOutlined } from '@ant-design/icons';
import { ModalForm, ProFormInstance, ProFormText } from '@ant-design/pro-components';
import { Button, message } from 'antd';
import { QRCodeSVG } from 'qrcode.react';
import { memo, useCallback, useEffect, useRef, useState } from 'react';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import {postAccountBindMyGoogleAuth, postAccountStartGoogleAuth} from "@/services/backend/adminAccountController";

const useBindAuth = () => {
  const [modalVisit, setModalVisit] = useState(false);
  const formRef = useRef<ProFormInstance>();

  const openModal = useCallback(() => {
    setModalVisit(true);
  }, []);

  const rules = [
    {
      required: true,
      message: '请填入验证码, 6位',
      min: 6,
      max: 6,
    },
  ];

  const InnerModelForm = memo(() => {
    const [authKey, setAuthKey] = useState('');
    const [loading, setLoading] = useState(true);
    const fetchUserInfo = useFetchUserInfo();

    useEffect(() => {
      async function getAuthKey() {
        let authKey = await postAccountStartGoogleAuth();
        formRef.current?.setFieldValue('authKey', authKey);
        setAuthKey(authKey);
        setLoading(false);
      }

      if (modalVisit) {
        getAuthKey();
      }
    }, [modalVisit]);

    return (
      <ModalForm<{
        authKey: string;
        authCode: number;
      }>
        title="绑定 Authenticator"
        open={modalVisit}
        onOpenChange={setModalVisit}
        autoFocusFirstInput
        formRef={formRef}
        modalProps={{
          destroyOnClose: true,
          width: '400px',
        }}
        loading={loading}
        onFinish={async (values) => {
          await postAccountBindMyGoogleAuth(values as API.BindGoogleAuthForm);
          message.success('绑定成功');
          await fetchUserInfo();
          setModalVisit(false);
        }}
      >
        <QRCodeSVG value={`otpauth://totp/theFernProAntd?secret=${authKey}`} />
        <ProFormText
          width="md"
          label="authKey"
          name="authKey"
          disabled
          fieldProps={{
            suffix: (
              <CopyToClipboard
                text={authKey}
                onCopy={(text, result) => {
                  if (result) {
                    message.success('Copy 成功');
                  } else {
                    message.warning('Copy 失败');
                  }
                }}
              >
                <Button icon={<CopyOutlined />} type={'text'}></Button>
              </CopyToClipboard>
            ),
          }}
        />
        <ProFormText.Password rules={rules} width="md" name="authCode" label="验证码" />
      </ModalForm>
    );
  });

  return {
    openModal,
    InnerModelForm,
  };
};

export default useBindAuth;
