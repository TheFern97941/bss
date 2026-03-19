import React, {useMemo} from "react";
import {ProColumns} from "@ant-design/pro-components";
import {isFunction} from "lodash";
import {ButtonType} from "antd/lib/button/buttonHelpers";
import {Action} from "@/components/MyProTable/components/Action";
import { MultiAction } from './MultiAction';


export type ProActionType = {
  reload: (resetPageIndex?: boolean) => Promise<void>;
}


export interface Command<T extends Record<PropertyKey, any>> {
  label?: React.ReactNode;
  icon?: React.ReactElement<any, any>;
  onHandle?: (t?: T, proAction?: ProActionType | undefined, selectedRows?: T[]) => Promise<any>;
  multiple?: boolean;
  showLabel?: boolean;
  showRowLabel?: boolean;
  tool?: boolean;//是否显示在工具栏
  row?: boolean;//是否显示在行内
  to?: ((t?: T) => string | undefined) | string; //跳转链接
  isEnable?: (t: T) => boolean;
  children?: (t?: T, proAction?: ProActionType, selectedRows?: T[]) => React.ReactElement | undefined;
  confirm?: boolean;
  danger?: boolean;
  type?: ButtonType;
}

export const DefaultTableAction: Partial<Command<any>> = {
  confirm: true,
  multiple: false,
  showLabel: true,
  showRowLabel: false,
  danger: false,
  tool: true,
  row: true,
  type: "default",
}


export interface ToActionProps<T extends Record<PropertyKey, any>> {
  action: Command<T>;
  proAction?: ProActionType;
  value: T;
  showLabel?: boolean;
}

export const ToAction: React.FC<ToActionProps<any>> = (props) => {
  const {action, proAction, value,showLabel = action.showRowLabel} = props;
  const href = useMemo(() => {
    if (!action.to) {
      return undefined;
    }
    if (isFunction(action.to)) {
      return action.to(value);
    }
    return action.to;
  }, [action.to, value]);

  return (
    <>
      <Action
        label={action.label}
        icon={action.icon}
        confirm={action.confirm}
        danger={action.danger}
        showLabel={showLabel}
        to={href}
        type={action.type}
        onClick={async () => {
          if (!action.children && action.onHandle) {
            try {
              await action.onHandle(value, proAction, []);
            } finally {
              proAction?.reload();
            }
          }
        }}
      >
        {action.children?.(value, proAction, [])}
      </Action>
    </>
  );
};

function toToolAction<T extends Record<PropertyKey, any>>(
  action: Command<T>, proAction: ProActionType | undefined
  , selectedRows: T[] | undefined) {
  const href = (function () {
    if (!action.to) {
      return undefined;
    }
    if (isFunction(action.to)) {
      return action.to(undefined);
    }
    return action.to;
  })();

  return <Action
    label={action.label}
    icon={action.icon}
    type={action.type}
    to={href}
    danger={action.danger}
    showLabel={action.showLabel}
    confirm={action.confirm}
    disabled={false}
    onClick={async () => {
      await action.onHandle?.(undefined, proAction, selectedRows)
      proAction?.reload()
    }}>
    {action.children?.(undefined, proAction, selectedRows)}
  </Action>
}

function toMultiAction<T extends Record<PropertyKey, any>, ValueType = 'text'>(
  action: Command<T>, proAction: ProActionType | undefined, columns: ProColumns<T, ValueType>[], selectedRows?: T[]
): React.ReactNode {
  return <MultiAction<T, ValueType>
    label={action.label}
    columns={columns}
    type={action.type}
    showLabel={action.showLabel}
    onReload={() => proAction?.reload()}
    selectRecords={selectedRows?.filter(r => action.isEnable?.(r) ?? true)}
    icon={action.icon}
    onHandle={async (t) => action?.onHandle?.(t, proAction, selectedRows)}
  />
}


export function toToolBarRender<T extends Record<PropertyKey, any>, ValueType = 'text'>(
  columns: ProColumns<T, ValueType>[],
  commands?: Command<T>[],
  elements: React.ReactNode[] = [],
) {
  return function (
    action: ProActionType | undefined,
    rows: {
      selectedRowKeys?: (string | number)[];
      selectedRows?: T[];
    }
  ) {
    const selectedRows = rows.selectedRows;
    if (!commands || !action) {
      return [...elements];
    }
    return [...elements, commands.filter(r => r.tool).map(actionProps => {
      if (actionProps.multiple) {
        return toMultiAction(actionProps, action, columns, selectedRows);
      } else {
        return toToolAction(actionProps, action, selectedRows);
      }
    })];
  }
}
