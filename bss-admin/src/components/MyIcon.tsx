import React from 'react';
import { Spin } from 'antd';

export interface MyIconProps {
  /** Fallback of icon if provided */
  children: React.ReactElement<any, any>;
  spin: boolean;
}


export function MyIcon({children, spin = false}: MyIconProps) {
  if (spin) {
    return <Spin indicator={<span className='anticon  ant-spin-dot'>{
      React.cloneElement(children, {
        width: "1em", height: "1em", fill: "currentColor", focusable: "false", className: 'anticon-spin'
      })
    }</span>}/>
  }
  return <span className='anticon'>
    {
      React.cloneElement(children, {
        width: "1em", height: "1em", fill: "currentColor", focusable: "false"
      })
    }
  </span>;
}
