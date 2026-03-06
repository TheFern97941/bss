import { Coin } from "./coin/Coin";

export interface ExchangeOrder {
  id: string;
  baseCoin: Coin;
  baseCoinNumber: number;
  // 价格
  price: number;

  targetCoin: Coin;
  // targetCoinNumber = baseCoinNumber * price
  targetCoinNumber: number;
  // fee = targetCoinNumber * fee
  fee: number;
}