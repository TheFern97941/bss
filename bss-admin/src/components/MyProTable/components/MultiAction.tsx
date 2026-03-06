import React, {useCallback, useEffect, useState} from 'react';
import {Alert, Space, Spin} from 'antd';
import {LoadingOutlined} from '@ant-design/icons';
import {ProColumns} from "@ant-design/pro-table";
import MyProTable from "@/components/MyProTable";
import isUndefined from "lodash/isUndefined";
import {ButtonType} from "antd/lib/button/buttonHelpers";
import { Actions } from './Actions';
import {Action, ActionProps} from "@/components/MyProTable/components/Action";
import { MyDrawer } from '@/components/MyDrawer';
import { MyIcon } from '@/components/MyIcon';


export type MultiActionDialogPropsType<T, ValueType = 'text'> = {
  selectRecords: T[] | undefined,
  columns: ProColumns<T, ValueType>[],
  action: ActionProps<T>,
  onReload: () => void,
  onHandle: (t: T) => Promise<any>;
  children?: React.ReactElement<any, any>;
};

export function MultiActionDialog<T extends Record<PropertyKey, any>, ValueType = 'text'>(props: MultiActionDialogPropsType<T, ValueType>) {
  const {selectRecords, onReload, columns, action, onHandle, children} = props;
  const [selectRecordsData, setSelectRecordsData] = useState<T[] | undefined>(selectRecords);
  const [confirmCashLoading, setConfirmCashLoading] = useState<boolean>(false);
  const [resultMap, setResultMap] = useState<Record<string, boolean>>({});
  const [message, setMessage] = useState<string>('');

  useEffect(() => setSelectRecordsData(selectRecords), [selectRecords]);

  const multiSubmit = useCallback(async () => {
    if (!(selectRecordsData && selectRecordsData.length)) {
      return;
    }
    try {
      setConfirmCashLoading(true);
      let success = 0;
      let error = 0;
      for (let i = 0; i < selectRecordsData.length; i++) {
        const item = selectRecordsData[i];
        let result = false;
        try {
          let r = await onHandle(item);
          if (r !== false) {
            success++;
            result = true;
          } else {
            error++;
            result = false;
          }
        }catch (e) {
          error++;
          result = false;
        }
        setResultMap((pv) => ({
          ...pv,
          [item.id]: result
        }));
        setSelectRecordsData([...selectRecordsData]);
      }
      setMessage(`成功${success}条, 失败${error}条 !`);
      // notification.info({
      //   message: '消息',
      //   description: `成功${success}条, 失败${error}条 !`,
      // });
      // eslint-disable-next-line no-empty
    } catch (e) {

    } finally {
      onReload();
      setConfirmCashLoading(false);
    }
  }, [selectRecordsData]);

  return <MyDrawer
    title="批量操作"
    footer={<Space>
      <Actions size={"middle"}>
        <Action
          type={"primary"} showLabel label={action.label} icon={action.icon}
          onClick={multiSubmit}/>
      </Actions>
      {
        message && <Alert message={message} type="success"/>
      }
    </Space>}
    onVisibleChange={(visible) => {
      if (!visible) {
        onReload();
        setResultMap({});
      }
    }}
    destroyOnClose
    trigger={children}
    bodyStyle={{padding: 0, margin: 0}}
  >
    <Spin spinning={confirmCashLoading}
          indicator={<LoadingOutlined style={{fontSize: 24}}/>}>
      <div>
        {
          (selectRecordsData && !!selectRecordsData.length) &&
          <MyProTable<T>
            search={false}
            rowSelection={false}
            toolBarRender={false}
            columns={[...columns, {
              title: '操作结果',
              fixed: 'right',
              width:120,
              render: (v, r) => {
                const resultMapElement = resultMap[r.id];
                if (isUndefined(resultMapElement)) {
                  return '';
                }
                return resultMapElement ?
                  <Alert style={{padding: "2px 4px"}} message="操作成功" banner type="success" showIcon/> :
                  <Alert style={{padding: "2px 4px"}} message="操作失败" banner type="error" showIcon/>;
              }
            },]}
            dataSource={selectRecordsData}
            rowKey={'id'}
            size="small"
            pagination={false}
          />
        }
      </div>
    </Spin>
  </MyDrawer>;
}


export interface MultiActionProps<T, ValueType = 'text'> {
  selectRecords: T[] | undefined,
  columns: ProColumns<T, ValueType>[],
  onHandle?: (t: T) => Promise<any>;
  icon?:  React.ReactElement<any, any>;
  label?: React.ReactNode;
  showLabel?: boolean;
  onReload: () => void;
  type?: ButtonType;
}

export function MultiAction<T extends Record<PropertyKey, any>, ValueType = 'text'>(props: MultiActionProps<T, ValueType>) {
  const {
    icon, onHandle, onReload, label,
    selectRecords, columns, showLabel,
    type = "primary"
  } = props;

  return <Action
    showLabel={showLabel}
    type={type}
    label={label}
    disabled={!selectRecords?.length}
    icon={icon && <MyIcon><>{icon}</></MyIcon>}>
    <MultiActionDialog
      selectRecords={selectRecords}
      columns={columns}
      action={{label, icon, onClick: onHandle}}
      onReload={onReload}
      onHandle={onHandle||(()=>Promise.resolve())}
    />
  </Action>
}
