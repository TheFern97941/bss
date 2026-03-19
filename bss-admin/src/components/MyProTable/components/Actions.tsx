import {Space} from "antd";
import React from "react";
import {SizeType} from "antd/es/config-provider/SizeContext";

export interface ActionsProps {
  children?: React.ReactNode | React.ReactNode[];
  size?: SizeType;
  direction?: "horizontal" | "vertical";
}

export function Actions({children, size = "small", direction = "horizontal"}: ActionsProps) {
  return <Space.Compact size={size} direction={direction}>
    {
      children
    }
  </Space.Compact>;
}
