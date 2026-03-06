import { ProFormDigit, ProFormGroup } from '@ant-design/pro-components';
import { postSysSettingAuthSetting } from '@/services/backend/sysSettingManagerController';
import MySettingForm from '@/pages/Sys/MainConfig/components/MySettingForm';

export default () => {
  return (
    <MySettingForm
      title={"授权设置"}
      recordId={'AuthSetting'}
      onSubmit={async (value, abortSignal) => {
        await postSysSettingAuthSetting(value, { abortSignal });
      }}
    >
      <ProFormGroup>
        <ProFormDigit name={'authLoginErrorMax'} label={'authLoginErrorMax'} required={true} />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormDigit name={'authLoginTokenMax'} label={'authLoginTokenMax'} required={true} />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormDigit name={'timeOutHours'} label={'timeOutHours'} required={true} />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormDigit name={'timeOutDelayMinutes'} label={'timeOutDelayMinutes'} required={true} />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormDigit name={'sessionTimeOutHours'} label={'sessionTimeOutHours'} required={true} />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormDigit name={'loginNeedAuthCodeCount'} label={'loginNeedAuthCodeCount'} required={true} />
      </ProFormGroup>
      <ProFormGroup>
        <ProFormDigit name={'sessionMax'} label={'sessionMax'} required={true} />
      </ProFormGroup>
    </MySettingForm>
  );
};
