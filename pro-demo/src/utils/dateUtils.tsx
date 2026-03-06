import dayjs, {isDayjs} from "dayjs";
import {isNumber} from "lodash";


export const BaseDateTimeTemplate = 'YYYY-MM-DD HH:mm:ss';
export const BaseDateTemplate = 'YYYY-MM-DD';


const SourceFormatTemplate = 'YYYY-MM-DDTHH:mm:ss.SSS';

export function toDayjs(date: string, timezone: string = "Asia/Shanghai") {
  if (/\.\d{3}$/.test(date)) {
    // 如果日期字符串以三位毫秒结束
    return dayjs.tz(date, SourceFormatTemplate, timezone);
  } else if (/\.\d{2}$/.test(date)) {
    // 如果日期字符串以一位或两位毫秒结束
    return dayjs.tz(date, 'YYYY-MM-DDTHH:mm:ss.SS', timezone);
  } else if (/\.\d{1}$/.test(date)) {
    // 如果日期字符串以一位或两位毫秒结束
    return dayjs.tz(date, 'YYYY-MM-DDTHH:mm:ss.S', timezone);
  } else if (isNumber(date)) {
    return dayjs(date);
  } else if (isDayjs(date)) {
    return date;
  } else {
    return dayjs.tz(date, BaseDateTimeTemplate, timezone);
  }
}

export function to(date: string | undefined, template: string = BaseDateTimeTemplate) {
  if (!date) return "";
  let data = toDayjs(date);
  return data
    .format(template);
}


export function toString(startValue: any) {
  // return dayjs(startValue).format(BaseDateTimeTemplate);
  return to(startValue);
}

export function toStringNotBlank(startValue: any) {
  return dayjs(startValue).format('YYYY-MM-DD_HH:mm:ss');
}

export function dateFormatter(value: dayjs.Dayjs, valueType: string): string | number {
  return toString(value);
}

export default {
  toString: toString,
  toStringNotBlank: toStringNotBlank,
};
