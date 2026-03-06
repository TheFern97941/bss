import React, {FC, useEffect, useState} from 'react';
import {InputNumber} from 'antd';
import type {InputNumberProps} from 'antd/es/input-number';
import amountUtils from '@/utils/amountUtils';

/**
 * MoneyInputProps:
 *  - value: 外部传入的金额数值(单位: 分)。可以是 number 或 null/undefined
 *  - onChange: 金额更新的回调，参数同样是以分为单位(number | null)
 *  - 其余 InputNumberProps(去掉了 value 和 onChange，由本组件内部管理这二者)
 */
export interface MoneyInputProps
  extends Omit<InputNumberProps, 'value' | 'onChange'> {
  value?: number | null;                          // 以分为单位
  onChange?: (value: number | null) => void;       // 回传时同样以分为单位
}



const AmountInput: FC<MoneyInputProps> = (props) => {
  const { value, onChange, onFocus, onBlur, ...rest } = props;  /**
   * internalValue：在输入框中使用元为单位进行显示/编辑；
   * 初始值根据外部的 `value`(分) 转换得到。
   */
  const [internalValue, setInternalValue] = useState<number | null>(
    (value !== null && value !== undefined) ? value / 100 : null
  );

  /**
   * isFocused：标记组件是否处于聚焦状态
   * 聚焦时不做过度格式化，方便用户编辑
   * 失焦时再对数值进行两位小数格式化
   */
  const [isFocused, setIsFocused] = useState(false);

  /**
   * isEqual：判断在两位小数格式下，两数是否相等；同时支持 null/undefined。
   *  - 若都为 null/undefined，则视为相等
   *  - 若一边有值，另一边没值，视为不相等
   *  - 否则，将两边都保留两位小数转为字符串后比较
   */
  const isEqual = (
    a: number | null | undefined,
    b: number | null | undefined
  ): boolean => {
    // 双双为 null 或 undefined
    const bothEmpty = (a === null || a === undefined) && (b === null || b === undefined);
    if (bothEmpty) {
      return true;
    }
    // 仅一方为空
    const oneEmpty = (a === null || a === undefined) || (b === null || b === undefined);
    if (oneEmpty) {
      return false;
    }
    // 均为数字，按两位小数进行比较
    return Number(a).toFixed(2) === Number(b).toFixed(2);
  };

  /**
   * 当外部的 value(分) 改变时，转换成元并同步到 internalValue
   * 前提是与当前 internalValue 不相等（两位小数层面上）
   */
  useEffect(() => {
    const externalYuan = (value !== null && value !== undefined) ? value / 100 : null;
    if (!isEqual(internalValue, externalYuan)) {
      setInternalValue(externalYuan);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [value]);

  /**
   * 处理用户输入变化:
   *  - 更新 internalValue(单位元)
   *  - 立即通过 onChange 把元转为分回调给外部
   */
  const handleChange = (val: number | string | null) => {
    // antd 的 InputNumber 可能返回 number、string 或 null
    // 做一次安全转换，无法解析成有效数字则设为 null
    let numericVal: number | null;
    if (typeof val === 'number') {
      numericVal = val;
    } else {
      const parsed = parseFloat(String(val));
      numericVal = isNaN(parsed) ? null : parsed;
    }

    setInternalValue(numericVal);

    // 回传给外部的依然是分
    if (onChange) {
      if (numericVal === null || numericVal === undefined) {
        onChange(null);
      } else {
        onChange(Math.round(numericVal * 100));
      }
    }
  };

  /**
   * 聚焦时标记 isFocused = true，保持原始输入
   */
  const handleFocus = (e: React.FocusEvent<HTMLInputElement>) => {
    setIsFocused(true);
    if (onFocus) onFocus(e);
  };

  /**
   * 失焦时，将内部值保留两位小数
   */
  const handleBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    setIsFocused(false);
    if (internalValue !== null && internalValue !== undefined && !isNaN(internalValue)) {
      setInternalValue(parseFloat(internalValue.toFixed(2)));
    }
    if (onBlur) onBlur(e);
  };
  return (
    <InputNumber
      value={internalValue}
      onChange={handleChange}
      /**
       * formatter:
       *  - 聚焦中：直接显示用户输入
       *  - 失焦时：保留两位小数，使用中国习惯的数字分隔方式
       */
      formatter={(val) => {
        if (val === null || val === undefined || val === '') return '';
        return isFocused
          ? String(val)
          : amountUtils.formatNumberToChinaStyle(Number(val), 2);
      }}
      min={0}
      /**
       * parser:
       *  - 去掉所有非数字和小数点字符，例如去掉分隔符等
       */
      parser={(val) => {
        if (!val) return '';
        return val.replace(/[^\d.]/g, '');
      }}
      {...rest}
      onFocus={handleFocus}
      onBlur={handleBlur}
    />
  );
};

export default AmountInput;
