"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Slider } from "@/components/ui/slider";
import { Badge } from "@/components/ui/badge";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { AlertTriangle, TrendingUp, TrendingDown } from "lucide-react";

export default function ContractPage() {
  const [leverage, setLeverage] = useState([10]);
  const [marginMode, setMarginMode] = useState("cross");
  const [positionMode, setPositionMode] = useState("hedge");
  const [orderType, setOrderType] = useState("limit");

  const positions = [
    {
      symbol: "BTCUSDT",
      side: "Long",
      size: "0.5 BTC",
      entryPrice: "42,500",
      markPrice: "43,256.50",
      pnl: "+756.25",
      pnlPercent: "+3.56%",
      margin: "2,125.00",
      leverage: "20x",
    },
    {
      symbol: "ETHUSDT",
      side: "Short",
      size: "10 ETH",
      entryPrice: "2,280",
      markPrice: "2,256.50",
      pnl: "+235.00",
      pnlPercent: "+1.03%",
      margin: "1,140.00",
      leverage: "10x",
    },
  ];

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold">合约交易</h1>
        <p className="text-muted-foreground mt-2">永续合约 & 交割合约</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-4">
        <div className="lg:col-span-8">
          <Card className="mb-4">
            <CardHeader>
              <div className="flex justify-between items-center">
                <div>
                  <CardTitle>BTCUSDT 永续</CardTitle>
                  <div className="flex gap-4 mt-2 text-sm">
                    <div>
                      <span className="text-muted-foreground">标记价格: </span>
                      <span className="font-semibold">43,256.50</span>
                    </div>
                    <div>
                      <span className="text-muted-foreground">指数价格: </span>
                      <span>43,255.80</span>
                    </div>
                    <div>
                      <span className="text-muted-foreground">资金费率: </span>
                      <span className="text-green-500">0.01%</span>
                    </div>
                    <div>
                      <span className="text-muted-foreground">倒计时: </span>
                      <span>05:23:15</span>
                    </div>
                  </div>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="aspect-video bg-muted rounded flex items-center justify-center mb-4">
                <p className="text-muted-foreground">K线图表区域</p>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>当前持仓</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left py-2 text-sm">合约</th>
                      <th className="text-left py-2 text-sm">方向</th>
                      <th className="text-right py-2 text-sm">持仓量</th>
                      <th className="text-right py-2 text-sm">开仓价</th>
                      <th className="text-right py-2 text-sm">标记价</th>
                      <th className="text-right py-2 text-sm">未实现盈亏</th>
                      <th className="text-right py-2 text-sm">保证金</th>
                      <th className="text-right py-2 text-sm">杠杆</th>
                      <th className="text-right py-2 text-sm">操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    {positions.map((position, i) => (
                      <tr key={i} className="border-b">
                        <td className="py-2 text-sm font-medium">{position.symbol}</td>
                        <td className="py-2 text-sm">
                          <Badge variant={position.side === "Long" ? "default" : "destructive"}>
                            {position.side === "Long" ? <TrendingUp className="w-3 h-3 mr-1" /> : <TrendingDown className="w-3 h-3 mr-1" />}
                            {position.side}
                          </Badge>
                        </td>
                        <td className="text-right py-2 text-sm">{position.size}</td>
                        <td className="text-right py-2 text-sm">{position.entryPrice}</td>
                        <td className="text-right py-2 text-sm">{position.markPrice}</td>
                        <td className="text-right py-2 text-sm">
                          <div className="text-green-500">
                            <div>{position.pnl} USDT</div>
                            <div className="text-xs">{position.pnlPercent}</div>
                          </div>
                        </td>
                        <td className="text-right py-2 text-sm">{position.margin}</td>
                        <td className="text-right py-2 text-sm">{position.leverage}</td>
                        <td className="text-right py-2 text-sm">
                          <Button size="sm" variant="outline">平仓</Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="lg:col-span-4">
          <Card>
            <CardHeader>
              <CardTitle>下单面板</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-2">
                  <Button variant="outline" className="data-[state=active]:bg-primary data-[state=active]:text-primary-foreground">
                    全仓
                  </Button>
                  <Button variant="outline">
                    逐仓
                  </Button>
                </div>

                <div>
                  <div className="flex justify-between mb-2">
                    <Label className="text-sm">杠杆倍数</Label>
                    <span className="text-sm font-bold">{leverage[0]}x</span>
                  </div>
                  <Slider 
                    value={leverage} 
                    onValueChange={setLeverage}
                    max={125}
                    min={1}
                    step={1}
                  />
                  <div className="flex justify-between text-xs text-muted-foreground mt-1">
                    <span>1x</span>
                    <span>25x</span>
                    <span>50x</span>
                    <span>75x</span>
                    <span>125x</span>
                  </div>
                </div>

                <Tabs defaultValue="limit">
                  <TabsList className="grid w-full grid-cols-3">
                    <TabsTrigger value="limit">限价</TabsTrigger>
                    <TabsTrigger value="market">市价</TabsTrigger>
                    <TabsTrigger value="trigger">条件</TabsTrigger>
                  </TabsList>
                  <TabsContent value="limit" className="space-y-4">
                    <div className="grid grid-cols-2 gap-2">
                      <Button className="bg-green-500 hover:bg-green-600">
                        开多 (买入)
                      </Button>
                      <Button className="bg-red-500 hover:bg-red-600">
                        开空 (卖出)
                      </Button>
                    </div>

                    <div>
                      <Label className="text-sm">价格</Label>
                      <Input type="number" placeholder="0.00 USDT" />
                    </div>

                    <div>
                      <Label className="text-sm">数量</Label>
                      <Input type="number" placeholder="0.00 BTC" />
                    </div>

                    <div className="space-y-2 p-3 bg-muted rounded">
                      <div className="flex justify-between text-sm">
                        <span>可用余额</span>
                        <span>10,000.00 USDT</span>
                      </div>
                      <div className="flex justify-between text-sm">
                        <span>最大可开</span>
                        <span>2.3123 BTC</span>
                      </div>
                      <div className="flex justify-between text-sm">
                        <span>保证金</span>
                        <span>-- USDT</span>
                      </div>
                      <div className="flex justify-between text-sm">
                        <span>手续费</span>
                        <span>-- USDT</span>
                      </div>
                    </div>

                    <div className="grid grid-cols-2 gap-2">
                      <Button variant="outline" size="sm">止盈止损</Button>
                      <Button variant="outline" size="sm">只减仓</Button>
                    </div>
                  </TabsContent>
                </Tabs>
              </div>
            </CardContent>
          </Card>

          <Card className="mt-4">
            <CardHeader>
              <CardTitle className="text-sm">账户信息</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex items-center justify-between p-2 bg-yellow-50 dark:bg-yellow-900/20 rounded">
                  <div className="flex items-center gap-2">
                    <AlertTriangle className="w-4 h-4 text-yellow-600" />
                    <span className="text-sm">风险率</span>
                  </div>
                  <span className="text-sm font-bold">15.23%</span>
                </div>
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">账户权益</span>
                    <span>12,456.78 USDT</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">已用保证金</span>
                    <span>3,265.00 USDT</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">可用保证金</span>
                    <span>9,191.78 USDT</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">未实现盈亏</span>
                    <span className="text-green-500">+991.25 USDT</span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}