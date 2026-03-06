// import {useClientConfig} from "@/hooks/MainConfigContext";
import isUndefined from "lodash/isUndefined";
import isNull from "lodash/isNull";
import toNumber from "lodash/toNumber";
import isString from "lodash/isString";
import amountUtils from "@/utils/amountUtils";

export interface AmountFieldProps {
  value: any;
  symbol: boolean;
  fractionDigits: number;
}

export default function AmountField(props: AmountFieldProps) {
  const {
    value,
    fractionDigits = AmountField.defaultProps.fractionDigits,
    symbol = true
  } = props;

  // const clientConfig = useClientConfig();

  if (isUndefined(value) || isNull(value)) {
    return ' - ';
  }
  if (isString(value)) {
    if (symbol) {
      //return value + clientConfig?.currencySymbol;
      return value;
    } else {
      return value;
    }
  }

  const formattedAmount = amountUtils.formatNumberToChinaStyle(toNumber(value) / 100.0, fractionDigits);

  if (symbol) {
    // return `${formattedAmount} ${clientConfig?.currencySymbol}`;
    return `${formattedAmount}`;
  } else {
    return formattedAmount;
  }
}

AmountField.defaultProps = {
  fractionDigits: 2,
  symbol: true,
}
