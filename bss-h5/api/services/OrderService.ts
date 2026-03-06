import {PageQuery} from "@/api/model/Common";
import {Coin} from "@/api/model/coin/Coin";


export async function lastOrders(count: number = 5): Promise<Coin[]> {
  const res = await fetch(`https://www.baidu.com`)
  return coins;
}

const coins: Coin[] = [
  {
    id: "0",
    symbol: "BTC",
    coinName: "Bitcoin",
    chain: "btc",
    isNative: true,
    decimals: 8,
    icon: "/coin/btc-icon.png"
  },
  {
    id: "1",
    symbol: "ETH",
    coinName: "Ethereum",
    chain: "ethereum",
    isNative: true,
    decimals: 8,
    icon: "/coin/eth-icon.png"
  },
  {
    id: "2",
    symbol: "USDT",
    coinName: "USDT Tather",
    chain: "ethereum",
    isNative: false,
    contractName: "ERC-20",
    contractAddress: "",
    decimals: 8,
    icon: "/coin/usdt-icon.png"
  },
  {
    id: "3",
    symbol: "USDT",
    coinName: "USDT Tather",
    chain: "Tron",
    contractName: "TRC-20",
    contractAddress: "",
    isNative: false,
    decimals: 8,
    icon: "/coin/usdt-icon.png"
  },
  {
    id: "4",
    symbol: "SOL",
    coinName: "Solana",
    chain: "solana",
    isNative: true,
    decimals: 8,
    icon: "/coin/sol-icon.png"
  },
]
