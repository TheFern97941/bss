import React, {useMemo} from 'react';
import type {DrawerProps} from 'antd';
import {Drawer} from 'antd';
import useMergedState from 'rc-util/es/hooks/useMergedState';
import {omit} from "lodash";

export type MyDrawerProps = DrawerProps & {
  children?: React.ReactElement<any, any> | React.ReactElement<any, any>[];
  trigger?: React.ReactElement<any, any>;
  onVisibleChange?: (visible: boolean) => void;
}


export function MyDrawer(props: MyDrawerProps) {
  const {width, trigger, open: propOpen, onVisibleChange} = props;

  const [open, setVisible] = useMergedState<boolean>(!!propOpen, {
    value: propOpen,
    onChange: onVisibleChange,
  });

  const triggerDom = useMemo(() => {
    if (!trigger) {
      return null;
    }

    return React.cloneElement(trigger, {
      key: 'trigger',
      ...trigger.props,
      onClick: async (e: any) => {
        setVisible(!open);
        trigger.props?.onClick?.(e);
      },
    });
  }, [setVisible, trigger, open]);

  return <>
    {triggerDom}
    <Drawer
      width={width || 1200}
      open={open}
      onClose={() => {
        setVisible(false);
      }}
      footer={false}
      {...omit(props, ['onVisibleChange', 'trigger'])}
    />
  </>;
}
