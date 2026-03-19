import {useEffect, useRef, useState} from 'react';
import {Grid, notification, Spin} from 'antd';
import useAbortController from '@/hooks/useAbortController';
import type { ModalFormProps, ProFormInstance } from '@ant-design/pro-components';
import {DrawerForm} from '@ant-design/pro-components';

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
  const {children, loading = false, record, onChange, request, trigger, message = true, onOpenChange, width, ...rest} = props;

  const abortSignal = useAbortController();
  const screens = Grid.useBreakpoint();
  const drawerWidth = (width || (screens.xxl || screens.xl || screens.lg ? '50%' : screens.md ? '80%' : '90%')) as string;
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
    width={drawerWidth}
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
    {...omit(rest, ['request', 'onChange', 'trigger', 'children', 'record', 'actionName', 'message', 'width'])}
  >
    <Spin spinning={loading} tip="正在加载..."> {children}</Spin>
  </DrawerForm>;
}
