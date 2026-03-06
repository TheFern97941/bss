import {PageQuery} from "@/api/model/Common";
import {Coin} from "@/api/model/coin/Coin";


export async function getSupportCoins(count: number = 5): Promise<Coin[]> {
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
    symbol: "XMR",
    coinName: "Monero",
    chain: "monero",
    isNative: true,
    decimals: 8,
    icon: "/coin/monero-icon.png"
  },
  {
    id: "3",
    symbol: "Tron",
    coinName: "Tron",
    chain: "Tron",
    isNative: true,
    decimals: 8,
    icon: "/coin/tron-icon.png"
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
