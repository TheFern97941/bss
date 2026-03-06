import isString from 'lodash/isString';
import toNumber from 'lodash/toNumber';
import isUndefined from 'lodash/isUndefined';
import isNull from 'lodash/isNull';
import AmountField from "@/components/Field/amount/AmountField";

let symbol = '¥';

/**
 * 将数字格式化为中国习惯的显示方式（每4位添加一个分隔符）
 * @param num 要格式化的数字
 * @param decimals 保留的小数位数
 * @returns 格式化后的字符串
 */
const formatNumberToChinaStyle = (num: number, decimals: number = 2): string => {
  // 将数字转为字符串，并保留指定小数位
  const numStr = num.toFixed(decimals);

  // 分割整数部分和小数部分
  const parts = numStr.split('.');
  const integerPart = parts[0];
  const decimalPart = parts.length > 1 ? '.' + parts[1] : '';

  // 从右向左每4位添加一个分隔符（中国习惯）
  const len = integerPart.length;
  let result = '';
  let counter = 0;

  for (let i = len - 1; i >= 0; i--) {
    counter++;
    result = integerPart.charAt(i) + result;
    if (counter % 4 === 0 && i !== 0) {
      result = ',' + result;
    }
  }

  // 返回格式化后的结果
  return result + decimalPart;
};
export function changeSymbol(_symbol: string) {
  symbol = _symbol;
}

const toString = (val: any, fractionDigits: number = 2) => {
  if (isUndefined(val) || isNull(val)) {
    return '';
  }
  // 使用formatNumberToChinaStyle代替简单的toFixed
  return formatNumberToChinaStyle(toNumber(val) / 100.0, fractionDigits);
};


const renderAmount = (val: any) => {
  return <AmountField value={val} symbol={false}/>;
};

const renderNotSymbol = (val: any) => {
  return <AmountField value={val} symbol={false}/>;
};

const toStringNotSymbol = (val: string | number | undefined, fractionDigits: number = 2) => {
  if (isUndefined(val) || isNull(val)) {
    return '';
  }
  // 使用formatNumberToChinaStyle代替简单的toFixed
  return formatNumberToChinaStyle(toNumber(val) / 100.0, fractionDigits);
};

function toValue(value: any) {
  if (isString(value)) {
    // 处理包含逗号的格式化数字
    const numString = value?.replace(symbol, '').trim().replace(/\s/, '').replace(/,/g, '');
    const number = Math.round(toNumber(numString) * 100);
    if (isNaN(number)) {
      return 0;
    }
    return number;
  }
  return 0;
}

const renderAmountText = (r: number) => {
  if (r < 0) {
    return '不支持';
  }
  return <AmountField value={r}/>;
};

const amountUtils = {
  formatNumberToChinaStyle: formatNumberToChinaStyle,
  renderAmountText: renderAmountText,
  renderAmount: renderAmount,
  renderNotSymbol: renderNotSymbol,
  toStringNotSymbol: toStringNotSymbol,
  toString: toString,
  toValue: toValue,
}
export default amountUtils;
