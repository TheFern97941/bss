export interface CoinMarketData {
  id: string;
  // 关联的币ID
  coinId: string;
  // 当前价格(USD)
  price: number;
  // 24小时涨跌幅(百分比)
  priceChange24h: number;
  // 24小时价格变化(USD)
  priceChangeAmount24h: number;
  // 24小时最高价
  high24h: number;
  // 24小时最低价
  low24h: number;
  // 市值(USD)
  marketCap: number;
  // 市值排名
  marketCapRank: number;
  // 24小时交易量(USD)
  volume24h: number;
  // 流通供应量
  circulatingSupply: number;
  // 总供应量
  totalSupply: number;
  // 最大供应量(null表示无限)
  maxSupply: number | null;
  // 完全稀释估值
  fullyDilutedValuation: number | null;
  // 历史最高价
  ath: number;
  // 历史最高价日期 (yyyy-MM-dd HH:mm:ss)
  athDate: string;
  // 距离历史最高价跌幅
  athChangePercentage: number;
  // 历史最低价
  atl: number;
  // 历史最低价日期 (yyyy-MM-dd HH:mm:ss)
  atlDate: string;
  // 距离历史最低价涨幅
  atlChangePercentage: number;
  // 数据来源
  dataSource: DataSource;
  // 最后更新时间 (yyyy-MM-dd HH:mm:ss)
  lastUpdated: string;
}

// 数据来源枚举
export enum DataSource {
  COINGECKO = 'CoinGecko',
  COINMARKETCAP = 'CoinMarketCap',
  BINANCE = 'Binance',
  INTERNAL = 'Internal'
}

// 价格历史记录(用于K线图)
export interface PriceHistory {
  id: string;
  coinId: string;
  timestamp: string;
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
  // 时间间隔类型
  interval: PriceInterval;
}

// 价格间隔枚举
export enum PriceInterval {
  MINUTE_1 = '1m',
  MINUTE_5 = '5m',
  MINUTE_15 = '15m',
  MINUTE_30 = '30m',
  HOUR_1 = '1h',
  HOUR_4 = '4h',
  DAY_1 = '1d',
  WEEK_1 = '1w',
  MONTH_1 = '1M'
}