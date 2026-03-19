import {
  ActionType,
  ParamsType,
  ProColumns,
  ProTable,
  ProTableProps,
  RequestData,
} from '@ant-design/pro-components';
import { Command, DefaultTableAction, ToAction, toToolBarRender } from './components/Command';
import {Button, Space, TableProps} from 'antd';
import { SortOrder } from 'antd/es/table/interface';
import { dateFormatter } from '@/utils/dateUtils';
import { useMemo, useState } from 'react';
import { Actions } from './components/Actions';
import AmountField from '@/components/Field/amount/AmountField';
import { LoadingOutlined, ReloadOutlined } from '@ant-design/icons';
import qs from 'qs';

export type ToolBarRenderType<RecordType extends Record<string, any>> = (action: ActionType | undefined, rows: {
  selectedRowKeys?: (string | number)[];
  selectedRows?: RecordType[];
}) => React.ReactNode[];

export type MyProTableProps<DataType extends Record<string, any>, Params extends ParamsType = ParamsType, ValueType = "text"> =
  Omit<ProTableProps<DataType, Params, ValueType>, 'columns' | 'toolBarRender'> & {
  columns?: ProColumns<DataType, ValueType>[];
  baseColumns?: ProColumns<DataType, ValueType>[];
  commands?: Command<DataType>[] ;
  toolBarRender?: ToolBarRenderType<DataType> | false;
  namespace?: string;
  optionActionDir?: "horizontal" | "vertical";
};

function MyProTable<
  DataType extends Record<string, any>,
  Params extends ParamsType = ParamsType,
  ValueType = "text"
>(props: MyProTableProps<DataType, Params, ValueType>) {

  const {
    columns,
    baseColumns,
    request,
    rowKey = "id",
    expandable = {},
    polling,
    rowSelection,
    commands: srcCommands,
    toolBarRender,
    namespace,
    optionActionDir = "horizontal",
    ...rest
  } = props;
  const {defaultExpandAllRows = false} = expandable;

  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([])

  const defaultPolling = typeof polling === 'number' ? polling : undefined;
  const [innerPolling, setInnerPolling] = useState<number | undefined>(defaultPolling);

  const curRowSelection: TableProps<DataType>['rowSelection'] = {
    columnWidth: 48,
    ...rowSelection
  };

  const createPollingButton = (isPolling: number | undefined) => (
    <Button
      key="polling"
      type="primary"
      onClick={() => {
        setInnerPolling(isPolling ? undefined : defaultPolling);
      }}
    >
      {isPolling ? <LoadingOutlined /> : <ReloadOutlined />}
      {isPolling ? '停止刷新' : '开始刷新'}
    </Button>
  );

  const commands = srcCommands?.map(r => ({...DefaultTableAction, ...r}))

  function getColumns() {
    return columns
      ?.map(r => {
        if (r.valueType === 'money') {
          return ({...r, valueType: 'text', renderText: (text) => <AmountField value={text} symbol={false}/>});
        } else if (r.valueType === 'dateTime') {
          return ({...r, valueType: 'text', renderText: (text) => text});
        } else if (r.valueType === 'digit') {
          return ({...r, valueType: 'text', renderText: (text) => text});
        } else if (r.valueType === 'option') {
          return {
            ...r, width: "min-content",
            fixed: "right",
          }
        }
        return ({...r});
      }) as ProColumns<DataType, ValueType>[];
  }

  const distColumns = useMemo(() => {
    let distColumns = getColumns();
    if (commands && commands.length > 0) {
      let optionsColumn: ProColumns<DataType, ValueType> = {
        title: '操作',
        valueType: 'option',
        width: "min-content",
        fixed: "right",
        align: "center",
        render: (_, record: DataType, i, action) => {
          return <Actions direction={optionActionDir}>
            {
              commands?.filter(a => (a.isEnable?.(record) ?? true) && a.row)
                .map((a, i) => {
                  return <ToAction key={i} action={a} proAction={action} value={record}/>
                })
            }
          </Actions>;
        },
      }
      return [...distColumns, optionsColumn];
    }
    return distColumns
  }, [columns]);

  const myToolBarRender = useMemo(() => {
    if (toolBarRender === false) {
      return false;
    }
    let render: ToolBarRenderType<DataType> = (action, rows) => {
      let originalButtons: React.ReactNode[];
      if (!toolBarRender) {
        originalButtons = [];
      } else {
        originalButtons = toolBarRender(action, rows) || [];
      }
      const element = polling ? createPollingButton(innerPolling) : [];

      const cColumns = baseColumns || columns;
      const baseButtons = cColumns ?
        toToolBarRender<DataType, ValueType>(cColumns, commands, originalButtons)(action, rows) :
        originalButtons;
      return [element, <Actions key={'actions'} size={'middle'}>{baseButtons}</Actions>];
    };
    return render;
  }, [toolBarRender, baseColumns, commands, polling, innerPolling]);


  function parseQueryString(queryString: string): Record<string, string> {
    const result: Record<string, any> = {};
    const pairs = queryString.split('&');

    for (const pair of pairs) {
      if(!pair){
        const [key, value] = pair.split('=');
        if(!value){
          result[decodeURIComponent(key)] = decodeURIComponent(value);
        }
      }
    }

    return result;
  }

  function filterQueryString(queryString: Record<string, any>): Record<string, any> {
    const q: Record<string, any> = {}
    Object.entries(queryString).forEach(([key, value]) => {
      if (!key.includes(".") && !key.startsWith('_')) {
        q[key] = value;
      }
    })
    return q;
  }

  const form: ProTableProps<DataType, ValueType>['form'] = useMemo(() => {
    if (!namespace) {
      return {syncToUrl: true}
    }
    return {
      // syncToUrl: true,
      syncToUrl: (params, type) => {
        const queryParams = qs.parse(location.search, {ignoreQueryPrefix: true, allowDots: true});
        if (type === 'get') {
          // 从URL参数中提取当前namespace的参数，过滤掉其他namespace
          let urlParams = queryParams[namespace] || {} as any;

          // 只保留以_开头的系统参数
          Object.keys(queryParams).forEach(key => {
            if (key.startsWith('_')) {
              urlParams[key] = queryParams[key];
            }
          });
          return urlParams;
        } else {
          // 清理所有namespace前缀的参数，只保留当前namespace参数和_开头的系统参数
          const cleanedParams: Record<string, any> = {};

          // 保留所有_开头的系统参数
          Object.keys(queryParams).forEach(key => {
            if (key.startsWith('_')) {
              cleanedParams[key] = queryParams[key];
            }
          });

          // 设置当前namespace的参数
          cleanedParams[namespace] = filterQueryString(params);

          // 将params中的_开头参数直接设置到顶层
          Object.keys(params).forEach(key => {
            if (key.startsWith('_')) {
              cleanedParams[key] = params[key];
            }
          });

          const paramsString = qs.stringify(cleanedParams, {allowDots: true});
          let data = parseQueryString(paramsString);
          return data;
        }
      },
    };
  }, [namespace]);

  async function innerRequest(params: any, sort: Record<string, SortOrder>, filter: Record<string, (string | number)[] | null>,): Promise<Partial<RequestData<DataType>>> {
    const result: Partial<RequestData<DataType>> = await request!!(params, sort, filter);
    if (defaultExpandAllRows) {
      // @ts-ignore
      const keys: string[] = [];

      function handler(data: DataType[]) {
        data.forEach((r) => {
          keys.push(r[rowKey as string]);
          if (r.children && r.children.length > 0) {
            handler(r.children);
          } else {
            // @ts-ignore
            delete r['children'];
          }
        });
      }

      if (result.data) {
        handler(result.data);
      }
      setExpandedRowKeys(keys);
    }
    return result;
  }


  return (
    <ProTable<DataType, Params, ValueType>
      scroll={{ x: 'max-content' }}
      cardBordered
      defaultSize="small"
      polling={innerPolling}
      rowKey={rowKey}
      form={form}
      toolBarRender={myToolBarRender}
      dateFormatter={dateFormatter}
      pagination={{
        pageSize: 25,
      }}
      tableAlertRender={({ selectedRowKeys = [] }) => (
        <Space size={24}>
          <span>已选 {selectedRowKeys.length} 项</span>
        </Space>
      )}
      search={{
        defaultCollapsed: true,
      }}
      rowSelection={rowSelection === false ? false : curRowSelection}
      columns={distColumns}
      {...(request ? {request: innerRequest} : {})}

      expandable={{
        defaultExpandAllRows,
        expandedRowKeys: expandedRowKeys,
        onExpandedRowsChange: (expandedRows) => {
          setExpandedRowKeys(expandedRows as string[]);
        },
        ...expandable,
      }}
      {...rest}
    />
  );
}

export default MyProTable;
