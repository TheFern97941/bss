import { ActionType } from '@ant-design/pro-components';
import { useRef } from 'react';
import SysModuleTableTree from '@/pages/Sys/SysModule/SysModuleTableTree';

export default () => {
  const actionRef = useRef<ActionType>();

  return <SysModuleTableTree />;
};
