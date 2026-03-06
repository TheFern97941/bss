
export interface Coin {
  id: string;
  // 币代号(BTC, ETH, USDT等)
  symbol: string;
  // 币的全称(Bitcoin, Ethereum等)  
  name: string;
  // 币的图标URL
  icon?: string;
  // 小数精度(显示精度)
  decimals: number;
  // 最小交易数量
  minTradeAmount: number;
  // 最大交易数量
  maxTradeAmount: number;
  // 最小提现数量
  minWithdrawAmount: number;
  // 最大提现数量
  maxWithdrawAmount: number;
  // 提现手续费
  withdrawFee: number;
  // 是否支持交易
  isTradable: boolean;
  // 是否支持充值
  isDepositable: boolean;
  // 是否支持提现
  isWithdrawable: boolean;
  // 币的类型
  coinType: CoinType;
  // 排序权重(用于列表排序)
  sortOrder: number;
  // 是否激活
  isActive: boolean;
  // 创建时间 (yyyy-MM-dd HH:mm:ss)
  createdAt: string;
  // 更新时间 (yyyy-MM-dd HH:mm:ss)
  updatedAt: string;
}

// 币类型枚举
export enum CoinType {
  CRYPTO = 'CRYPTO',        // 加密货币
  FIAT = 'FIAT',           // 法定货币
  STABLECOIN = 'STABLECOIN' // 稳定币
}

// 辅助函数：判断是否为稳定币
export function isStablecoin(coinType: CoinType): boolean {
  return coinType === CoinType.STABLECOIN;
}

// 辅助函数：判断是否为法币
export function isFiat(coinType: CoinType): boolean {
  return coinType === CoinType.FIAT;
}

// 辅助函数：判断是否为加密货币（非稳定币）
export function isCrypto(coinType: CoinType): boolean {
  return coinType === CoinType.CRYPTO;
}