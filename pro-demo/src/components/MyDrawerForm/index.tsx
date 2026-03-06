import {useEffect, useRef, useState} from 'react';
import {notification, Spin} from 'antd';
import useAbortController from '@/hooks/useAbortController';
import type {ProFormInstance} from '@ant-design/pro-form';
import {DrawerForm} from '@ant-design/pro-form';
import type {ModalFormProps} from '@ant-design/pro-form/lib/layouts/ModalForm';
import { omit, isString } from 'lodash';

export type MyDrawerFormProps<T = Record<string, any>> = Omit<ModalFormProps<T>, 'request'> & {
  children?: React.ReactNode | false;
  trigger?: React.ReactElement<any, any> | React.ReactElement<any, any>[];
  record?: T;
  actionName?: string;
  message?: boolean | string;
  onChange?: () => void;
  request?: (signal: AbortSignal, params: T) => Promise<any>;
}


export function MyDrawerForm<T = Record<string, any>>(props: MyDrawerFormProps<T>) {
  const {children, loading = false, record, onChange, request, trigger, message = true, onOpenChange, ...rest} = props;

  const abortSignal = useAbortController();
  const actionName = props.actionName || (!!record ? '修改' : '添加');
  const formRef = useRef<ProFormInstance>();
  const [modalVisible, setModalVisible] = useState<boolean>(false);

  useEffect(() => {
    onOpenChange?.(modalVisible);
  }, [modalVisible]);

  useEffect(() => {
    if (record) {
      formRef.current?.resetFields();
      formRef.current?.setFieldsValue(record);
    }
    if (!modalVisible) {
      setTimeout(() => {
        formRef.current?.resetFields();
      }, 500);
    }
  }, [record, modalVisible, formRef]);



  return <DrawerForm<T>
    autoFocusFirstInput
    open={modalVisible}
    onOpenChange={setModalVisible}
    trigger={trigger}
    formRef={formRef}
    // width={{'xxl': "50%", 'xl': "50%", 'lg': "50%" , 'md': "80%" , 'sm': "90%"}}
    // width={'max-content'}
    onFinish={async (store) => {
      const form = {
        ...record,
        ...store,
      };
      await request?.(abortSignal, form);
      if (message) {
        if (isString(message)) {
          notification.info({
            message: '消息',
            description: message,
          });
        } else {
          notification.info({
            message: '消息',
            description: `${actionName}成功`,
          });
        }
      }
      onChange?.();
      return true;
    }}
    {...omit(rest, ['request', 'onChange', 'trigger', 'children', 'record', 'actionName', 'message'])}
  >
    <Spin spinning={loading} tip="正在加载..."> {children}</Spin>
  </DrawerForm>;
}
