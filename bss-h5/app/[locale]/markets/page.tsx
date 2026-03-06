"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { TrendingUp, TrendingDown, DollarSign, Activity } from "lucide-react";

export default function MarketPage() {
  const [sortBy, setSortBy] = useState("volume");
  
  const mockData = [
    { symbol: "BTC/USDT", price: "43,256.50", change: 2.34, volume: "1,234.56M", high: "44,100.00", low: "42,800.00" },
    { symbol: "ETH/USDT", price: "2,256.50", change: -1.23, volume: "456.78M", high: "2,300.00", low: "2,230.00" },
    { symbol: "BNB/USDT", price: "315.20", change: 3.45, volume: "234.56M", high: "320.00", low: "310.00" },
    { symbol: "SOL/USDT", price: "98.45", change: -0.56, volume: "123.45M", high: "100.00", low: "97.00" },
  ];

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold">行情中心</h1>
        <p className="text-muted-foreground mt-2">实时追踪市场动态</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">24H成交量</CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$2.45B</div>
            <p className="text-xs text-muted-foreground">+12.5% from yesterday</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">涨幅榜首</CardTitle>
            <TrendingUp className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">DOGE</div>
            <p className="text-xs text-green-500">+45.2%</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">跌幅榜首</CardTitle>
            <TrendingDown className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">SHIB</div>
            <p className="text-xs text-red-500">-12.3%</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">活跃交易对</CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">156</div>
            <p className="text-xs text-muted-foreground">实时交易中</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <div className="flex justify-between items-center">
            <CardTitle>市场行情</CardTitle>
            <div className="flex gap-2">
              <Button 
                variant={sortBy === "volume" ? "default" : "outline"} 
                size="sm"
                onClick={() => setSortBy("volume")}
              >
                成交量
              </Button>
              <Button 
                variant={sortBy === "change" ? "default" : "outline"} 
                size="sm"
                onClick={() => setSortBy("change")}
              >
                涨跌幅
              </Button>
              <Button 
                variant={sortBy === "price" ? "default" : "outline"} 
                size="sm"
                onClick={() => setSortBy("price")}
              >
                最新价
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <Tabs defaultValue="all" className="w-full">
            <TabsList>
              <TabsTrigger value="all">全部</TabsTrigger>
              <TabsTrigger value="spot">现货</TabsTrigger>
              <TabsTrigger value="futures">合约</TabsTrigger>
              <TabsTrigger value="favorites">自选</TabsTrigger>
            </TabsList>
            <TabsContent value="all">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>币种</TableHead>
                    <TableHead className="text-right">最新价</TableHead>
                    <TableHead className="text-right">24H涨跌</TableHead>
                    <TableHead className="text-right">24H最高</TableHead>
                    <TableHead className="text-right">24H最低</TableHead>
                    <TableHead className="text-right">24H成交量</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockData.map((item) => (
                    <TableRow key={item.symbol}>
                      <TableCell className="font-medium">{item.symbol}</TableCell>
                      <TableCell className="text-right">${item.price}</TableCell>
                      <TableCell className={`text-right ${item.change > 0 ? "text-green-500" : "text-red-500"}`}>
                        {item.change > 0 ? "+" : ""}{item.change}%
                      </TableCell>
                      <TableCell className="text-right">${item.high}</TableCell>
                      <TableCell className="text-right">${item.low}</TableCell>
                      <TableCell className="text-right">${item.volume}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
}