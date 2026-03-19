import { useModelInitState } from '@/hooks/useModelInitState';
import {
  PageContainer,
  ProForm,
  ProFormGroup,
  ProFormText,
  ProFormUploadButton,
} from '@ant-design/pro-components';
import { Button, Card, message, theme } from 'antd';
import React, { useState } from 'react';
import { Result } from '@/requestErrorConfig';
import { redirectByTimeout } from '@/utils/redirectUtil';
import { getToken } from '@/utils/authUtil';
import { useFetchUserInfo } from '@/hooks/useFetchUserInfo';
import {postAccount} from "@/services/backend/adminAccountController";

export default () => {
  const [readonly, setReadonly] = useState(false);

  const { initialState } = useModelInitState();
  const initialUser = initialState?.currentUser || {} as API.AdminModel;

  const currentUser = {
    ...initialUser,
    avatar: initialUser?.avatar
      ? [
          {
            url: initialUser?.avatar,
            name: 'avatar',
            status: 'done',
          },
        ]
      : [],
  };

  const fetchUserInfo = useFetchUserInfo();
  const { token } = theme.useToken();

  return (
    <PageContainer
      header={{
        title: '个人中心',
        breadcrumb: {
          items: [
            {
              path: '',
              title: '个人中心',
            },
          ],
        },
      }}
    >
      <Card>
        <div
          style={{
            fontSize: '20px',
            color: token.colorTextHeading,
          }}
        >
          个人中心
        </div>
        <Button
          style={{
            marginBlockEnd: 16,
          }}
          onClick={() => setReadonly(!readonly)}
        >
          {readonly ? '编辑' : '只读'}
        </Button>
        <ProForm
          readonly={readonly}
          name="userCenterForm"
          initialValues={currentUser}
          onFinish={async (value) => {
            await postAccount(value);
            setReadonly(true);
            await fetchUserInfo();
          }}
        >
          <ProFormGroup>
            <ProFormText width="md" name="id" label="id" readonly={true} />
            <ProFormText width="md" name="username" label="username" readonly={true} />
          </ProFormGroup>
          <ProFormText width="md" name="name" label="name" />
          <ProFormText width="md" name="title" label="title" />
          <ProFormText width="md" name="email" label="email" />
          <ProFormUploadButton
            width="md"
            name="avatar"
            label="avatar"
            max={1}
            accept="image/*"
            action={'/api/fileUpload'}
            fieldProps={{
              headers: { Authorization: getToken() || '' },
            }}
            transform={(value) => {
              console.log('transform: ', value);
              return {
                avatar: value?.[0]?.response?.data,
              };
            }}
            onChange={(info) => {
              if (info.file.status === 'done') {
                console.log('上传结束'); // 后端返回的路径
                const res = info.file.response as unknown as Result<string>;
                if (!res.success) {
                  console.log('上传失败');

                  if (res.errorMessage) message.error(res.errorMessage);
                  if (res.errorCode === 'token.timeOut') {
                    redirectByTimeout();
                  }
                }
              }
            }}
          />
        </ProForm>
      </Card>
    </PageContainer>
  );
};
