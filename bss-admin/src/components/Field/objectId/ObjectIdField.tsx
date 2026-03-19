import { Tooltip, Typography } from 'antd';
import type { ReactElement } from 'react';
import type { ColumnType } from 'antd/lib/table/interface';
import { Link } from "@@/exports";
import { ProColumns } from '@ant-design/pro-components';

const { Paragraph } = Typography;

/**
 * 格式化 Object ID，仅显示最后6位
 * @param id Object ID 字符串
 * @returns 格式化后的 Object ID
 */
const formatObjectId = (id: string): string => {
  if (!id) return '';
  return id.length > 6 ? `${id.slice(-6)}` : id;
};

interface ObjectIdFieldProps {
  title: string;
}

export function ObjectIdField({ title }: ObjectIdFieldProps) {
  const formattedId = formatObjectId(title);
  return (
    <Tooltip title={title}>
      <Paragraph
        style={{ marginBottom: 0 }}
        copyable={{ text: title }}
      >
        {formattedId}
      </Paragraph>
    </Tooltip>
  );
}

interface ObjectIdLinkFieldProps {
  title: string;
  href: string;
  target?: string;
}

export function ObjectIdLinkField({ title, href, target }: ObjectIdLinkFieldProps) {
  const formattedId = formatObjectId(title);
  return (
    <Tooltip title={title}>
      <Paragraph
        style={{ marginBottom: 0 }}
        copyable={{ text: title }}
      >
        <Link
          style={{ marginBottom: 0 }}
          to={href}
          target={target}
        >
          {formattedId}
        </Link>
      </Paragraph>
    </Tooltip>
  );
}

/**
 * 创建一个包含 Object ID 的 ProColumns 配置
 * @param field 数据字段名，默认为 "id"
 * @param proColumns 额外的 ProColumns 配置
 * @param link 可选的链接前缀
 * @returns ProColumns 配置对象
 */
export const objectIdField = (
  field: string = "id",
  proColumns?: ProColumns,
  link?: string
): ProColumns => {
  return {
    title: 'ID',
    dataIndex: field,
    width: '120px', // 根据格式化后的长度调整宽度
    hideInSearch: true,
    renderText: (text: string, record: any) => {
      if (link) {
        return <ObjectIdLinkField title={text} href={`${link}${text}`} />;
      }
      return <ObjectIdField title={text} />;
    },
    ...proColumns
  };
};

export interface ObjectIdLinkRenderType<T> {
  (field: any, row: T, child: ReactElement): ReactElement;
}

/**
 * 创建一个自定义渲染的 Object ID 列
 * @param field 数据字段名
 * @param render 自定义渲染函数
 * @param columns 额外的 ProColumns 配置
 * @returns ProColumns 配置对象
 */
export function objectIdLinkFieldCustom<T>(
  field: string,
  render: ObjectIdLinkRenderType<T>,
  columns?: ProColumns<T>
): ProColumns<T> {
  return {
    title: '标识',
    dataIndex: field,
    width: '120px', // 根据格式化后的长度调整宽度
    hideInSearch: true,
    renderText: (text: string, record: T) => {
      const formattedId = formatObjectId(text);
      return (
        <Tooltip title={text}>
          {render(text, record, <>{formattedId}</>)}
        </Tooltip>
      );
    },
    ...columns
  };
}

/**
 * 创建一个简单的 Object ID 列配置
 * @param field 数据字段名
 * @param columns 额外的 ColumnType 配置
 * @returns ColumnType 配置对象
 */
export const objectIdColumnField = (
  field: string,
  columns?: ColumnType<any>
): ColumnType<any> => {
  return {
    title: field,
    dataIndex: field,
    width: '120px', // 根据格式化后的长度调整宽度
    render: (text: string) => {
      const formattedId = formatObjectId(text);
      return (
        <Tooltip title={text}>
          <Paragraph
            style={{ marginBottom: 0 }}
            copyable={{ text }}
          >
            {formattedId}
          </Paragraph>
        </Tooltip>
      );
    },
    ...columns
  };
};
