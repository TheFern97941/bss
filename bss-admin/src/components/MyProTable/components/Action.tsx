import {Button, Popconfirm, Tooltip} from "antd";
import {MyIcon} from "@/components/MyIcon";
import React from "react";
import LoadingButton from "@/components/LoadingButton";
import {ButtonType} from "antd/lib/button/buttonHelpers";
import {Link} from "@@/exports";
import type {RenderFunction} from "antd/es/_util/getRenderPropValue";


export interface ActionButtonProps {
  icon?: React.ReactElement<any, any>;
  label?: React.ReactNode;
  confirm?: boolean;
  confirmTitle?: React.ReactNode | RenderFunction;
  showLabel?: boolean;
  disabled?: boolean;
  danger?: boolean;
  type?: ButtonType;
  to?: string;
  onClick?: () => Promise<void>
}


function ActionButton(props: ActionButtonProps) {
  const {icon, onClick, label, confirmTitle, confirm, showLabel, type, disabled, to, danger} = props;
  if (confirm) {
    return <Popconfirm
      title={confirmTitle || label}
      onConfirm={async ()=>{
        try {
          await onClick?.()
        } catch (e) {
          console.error(e)
        }
      }}
    >
      <Tooltip title={label}>
        <Button danger={danger} href={to} disabled={disabled} type={type} icon={icon && <MyIcon>{icon}</MyIcon>}>
          {showLabel && label}
        </Button>
      </Tooltip>
    </Popconfirm>
  } else {
    if (to) {
      return <Link to={to}>
        <Button danger={danger} disabled={disabled} type={type} icon={icon && <MyIcon>{icon}</MyIcon>}>
          {showLabel && label}
        </Button>
      </Link>
    }
    return <Tooltip title={label}>
      <LoadingButton danger={danger} href={to} disabled={disabled} type={type} onClick={onClick} icon={icon && <MyIcon>{icon}</MyIcon>}>
        {showLabel && label}
      </LoadingButton>
    </Tooltip>
  }
}

export interface ActionProps<T> {
  value?: T;
  icon?: React.ReactElement<any, any>;
  label?: React.ReactNode;
  children?: React.ReactElement;
  confirm?: boolean;
  showLabel?: boolean;
  disabled?: boolean;
  danger?: boolean;
  type?: ButtonType;
  to?: string;
  confirmTitle?: React.ReactNode | RenderFunction;
  onClick?: (t: T) => Promise<void>
}

export function Action<T = any>(props: ActionProps<T>) {
  const {
    icon, children, onClick,
    label, confirm, confirmTitle, showLabel = false,
    disabled = false,
    danger = false,
    value, type,
    to
  } = props;
  if (children) {
    return React.cloneElement(children, {
      children: <ActionButton
        disabled={disabled}
        type={type}
        confirmTitle={confirmTitle}
        showLabel={showLabel}
        confirm={confirm}
        label={label}
        danger={danger}
        onClick={async () => {
           await onClick?.(value as T)
        }}
        icon={icon && <MyIcon>{icon}</MyIcon>}
        to={to}
      />
    });
  } else {
    return <ActionButton
      type={type}
      confirmTitle={confirmTitle}
      disabled={disabled}
      showLabel={showLabel}
      confirm={confirm}
      label={label}
      danger={danger}
      onClick={async () => {
        await onClick?.(value as T)
      }}
      icon={icon && <MyIcon>{icon}</MyIcon>}
      to={to}
    />;
  }
}



