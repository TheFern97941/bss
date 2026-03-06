"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Slider } from "@/components/ui/slider";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

export default function TradePage() {
  const [orderType, setOrderType] = useState("limit");
  const [tradeType, setTradeType] = useState("buy");
  const [amount, setAmount] = useState("");
  const [price, setPrice] = useState("");
  const [percentage, setPercentage] = useState([0]);

  const orderBook = {
    asks: [
      { price: "43,260.50", amount: "0.1234", total: "5,338.67" },
      { price: "43,261.00", amount: "0.5678", total: "24,563.60" },
      { price: "43,262.50", amount: "0.2345", total: "10,145.08" },
      { price: "43,263.00", amount: "0.8901", total: "38,497.46" },
      { price: "43,265.50", amount: "0.3456", total: "14,952.56" },
    ],
    bids: [
      { price: "43,256.50", amount: "0.4567", total: "19,755.34" },
      { price: "43,255.00", amount: "0.7890", total: "34,128.19" },
      { price: "43,254.50", amount: "0.2345", total: "10,143.18" },
      { price: "43,253.00", amount: "0.5678", total: "24,557.01" },
      { price: "43,250.50", amount: "0.9012", total: "38,979.33" },
    ],
  };

  const recentTrades = [
    { time: "15:23:45", price: "43,256.50", amount: "0.1234", type: "buy" },
    { time: "15:23:42", price: "43,257.00", amount: "0.5678", type: "sell" },
    { time: "15:23:38", price: "43,255.50", amount: "0.2345", type: "buy" },
    { time: "15:23:35", price: "43,256.00", amount: "0.8901", type: "sell" },
  ];

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold">现货交易</h1>
        <p className="text-muted-foreground mt-2">BTC/USDT 永续合约</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-4">
        <div className="lg:col-span-3">
          <Card>
            <CardHeader>
              <CardTitle className="text-sm">盘口</CardTitle>
            </CardHeader>
            <CardContent className="p-0">
              <div className="space-y-0">
                <div className="px-4 pb-2">
                  <div className="text-xs text-muted-foreground grid grid-cols-3 gap-2">
                    <span>价格(USDT)</span>
                    <span className="text-right">数量(BTC)</span>
                    <span className="text-right">累计</span>
                  </div>
                </div>
                <div className="space-y-1 px-4">
                  {orderBook.asks.reverse().map((ask, i) => (
                    <div key={i} className="grid grid-cols-3 gap-2 text-xs relative">
                      <div className="absolute inset-0 bg-red-500/10" style={{ width: `${(i + 1) * 20}%` }}></div>
                      <span className="text-red-500 relative">{ask.price}</span>
                      <span className="text-right relative">{ask.amount}</span>
                      <span className="text-right relative">{ask.total}</span>
                    </div>
                  ))}
                </div>
                <div className="py-2 px-4 border-y">
                  <div className="text-lg font-bold text-green-500">43,256.50</div>
                  <div className="text-xs text-muted-foreground">≈ $43,256.50</div>
                </div>
                <div className="space-y-1 px-4 pt-1">
                  {orderBook.bids.map((bid, i) => (
                    <div key={i} className="grid grid-cols-3 gap-2 text-xs relative">
                      <div className="absolute inset-0 bg-green-500/10" style={{ width: `${(5 - i) * 20}%` }}></div>
                      <span className="text-green-500 relative">{bid.price}</span>
                      <span className="text-right relative">{bid.amount}</span>
                      <span className="text-right relative">{bid.total}</span>
                    </div>
                  ))}
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="lg:col-span-6">
          <Card className="mb-4">
            <CardContent className="p-4">
              <div className="aspect-video bg-muted rounded flex items-center justify-center">
                <p className="text-muted-foreground">K线图表区域</p>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="text-sm">最新成交</CardTitle>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="text-xs">时间</TableHead>
                    <TableHead className="text-xs">价格</TableHead>
                    <TableHead className="text-xs">数量</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {recentTrades.map((trade, i) => (
                    <TableRow key={i}>
                      <TableCell className="text-xs">{trade.time}</TableCell>
                      <TableCell className={`text-xs ${trade.type === "buy" ? "text-green-500" : "text-red-500"}`}>
                        {trade.price}
                      </TableCell>
                      <TableCell className="text-xs">{trade.amount}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </div>

        <div className="lg:col-span-3">
          <Card>
            <CardHeader>
              <CardTitle>下单</CardTitle>
            </CardHeader>
            <CardContent>
              <Tabs value={tradeType} onValueChange={setTradeType}>
                <TabsList className="grid w-full grid-cols-2">
                  <TabsTrigger value="buy" className="data-[state=active]:bg-green-500 data-[state=active]:text-white">
                    买入
                  </TabsTrigger>
                  <TabsTrigger value="sell" className="data-[state=active]:bg-red-500 data-[state=active]:text-white">
                    卖出
                  </TabsTrigger>
                </TabsList>
                <TabsContent value={tradeType} className="space-y-4">
                  <div>
                    <Label className="text-xs">订单类型</Label>
                    <Select value={orderType} onValueChange={setOrderType}>
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="limit">限价单</SelectItem>
                        <SelectItem value="market">市价单</SelectItem>
                        <SelectItem value="stop">止损单</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>

                  {orderType === "limit" && (
                    <div>
                      <Label className="text-xs">价格</Label>
                      <Input 
                        type="number" 
                        placeholder="0.00 USDT"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                      />
                    </div>
                  )}

                  <div>
                    <Label className="text-xs">数量</Label>
                    <Input 
                      type="number" 
                      placeholder="0.00 BTC"
                      value={amount}
                      onChange={(e) => setAmount(e.target.value)}
                    />
                  </div>

                  <div>
                    <div className="flex justify-between text-xs mb-2">
                      <span>金额</span>
                      <span>{percentage[0]}%</span>
                    </div>
                    <Slider 
                      value={percentage} 
                      onValueChange={setPercentage}
                      max={100}
                      step={25}
                      className="mb-2"
                    />
                    <div className="flex justify-between text-xs text-muted-foreground">
                      <span>0%</span>
                      <span>25%</span>
                      <span>50%</span>
                      <span>75%</span>
                      <span>100%</span>
                    </div>
                  </div>

                  <div className="space-y-2 text-xs">
                    <div className="flex justify-between">
                      <span>可用</span>
                      <span>10,000.00 USDT</span>
                    </div>
                    <div className="flex justify-between">
                      <span>最大可买</span>
                      <span>0.2312 BTC</span>
                    </div>
                  </div>

                  <Button 
                    className={`w-full ${tradeType === "buy" ? "bg-green-500 hover:bg-green-600" : "bg-red-500 hover:bg-red-600"}`}
                  >
                    {tradeType === "buy" ? "买入 BTC" : "卖出 BTC"}
                  </Button>
                </TabsContent>
              </Tabs>
            </CardContent>
          </Card>

          <Card className="mt-4">
            <CardHeader>
              <CardTitle className="text-sm">持仓</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">BTC</span>
                  <span>0.1234</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">USDT</span>
                  <span>10,000.00</span>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}