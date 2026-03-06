import {
  getSysSetting,
} from '@/services/backend/sysSettingManagerController';
import {PageContainer, ProForm} from '@ant-design/pro-components';
import { message } from 'antd';
import React, {useState} from 'react';
import useAbortController from "@/hooks/useAbortController";

export interface MySettingFormProps {
  title?: string;
  recordId: string;
  children?: React.ReactNode;
  onSubmit: (value: any, abortSignal: AbortSignal) => Promise<void>;
}

const MySettingForm = ({ title = '系统设置', recordId, children, onSubmit }: MySettingFormProps) => {

  const abortSignal = useAbortController();
  const [formKey, setFormKey] = useState(0);

  return (
    <PageContainer
      header={{
        title: title,
      }}
    >
      <ProForm<any>
        key={formKey}
        name="mainConfirgForm"
        request={async () => {
          const setting = await getSysSetting({ id: recordId }, { abortSignal });
          return setting.setting;
        }}
        onFinish={async (value) => {
          await onSubmit(value, abortSignal);
          setFormKey(k => k + 1);
          message.success('保存成功! ');
        }}
      >
        {children}
      </ProForm>
    </PageContainer>
  );
};

export default MySettingForm;
