export interface Contract {
  id: string;
  // 币的ID (关联Coin)
  coinId: string;
  // 合约类型 (ERC-20, TRC-20, BEP-20等)
  contractType: string;
  // 合约地址
  contractAddress: string;
  // 链的名字(Ethereum, BSC, TRON等)
  chain: string;
  // 小数精度
  decimals: number;
  // 是否是主链原生代币
  isNative: boolean;
  // 合约创建时间 (yyyy-MM-dd HH:mm:ss)
  createdAt?: string;
  // 合约验证状态
  isVerified?: boolean;
}

// 合约类型枚举
export enum ContractType {
  ERC20 = 'ERC-20',
  TRC20 = 'TRC-20',
  BEP20 = 'BEP-20',
  SPL = 'SPL',
  NATIVE = 'NATIVE'
}

// 支持的区块链枚举
export enum Blockchain {
  ETHEREUM = 'Ethereum',
  BSC = 'BSC',
  TRON = 'TRON',
  SOLANA = 'Solana',
  POLYGON = 'Polygon',
  ARBITRUM = 'Arbitrum',
  OPTIMISM = 'Optimism',
  AVALANCHE = 'Avalanche'
}