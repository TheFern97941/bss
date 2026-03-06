import {ProColumns} from "@ant-design/pro-components";

const createdDate: ProColumns<any>[] = [
  {
    title: '创建时间',
    key: 'showTime',
    dataIndex: 'createDate',
    valueType: 'dateTime',
    sorter: true,
    defaultSortOrder: "descend",
    hideInSearch: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createDate',
    valueType: 'dateTimeRange',
    hideInTable: true,
    search: {
      transform: (value) => {
        return {
          createdStartTime: value[0],
          createdEndTime: value[1],
        };
      },
    },
  },
]

const updatedDate: ProColumns = {
    title: '更新时间',
    key: 'showTime',
    dataIndex: 'updateDate',
    valueType: 'dateTime',
    hideInSearch: true,
  }

export const defaultColumns = [
  ...createdDate,
  updatedDate,
]
