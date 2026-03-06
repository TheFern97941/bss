"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import { Heart, MessageCircle, Share2, TrendingUp, Star, Users, Trophy, Target, BarChart3, BookOpen } from "lucide-react";

export default function SquarePage() {
  const [activeTab, setActiveTab] = useState("discover");

  const posts = [
    {
      id: 1,
      author: {
        name: "加密分析师",
        avatar: "/avatar1.png",
        badge: "专业交易员",
        followers: "12.5K",
      },
      content: "BTC突破关键阻力位43,500，下一目标看向45,000。技术面显示强势上涨信号，MACD金叉，RSI未进入超买区。建议逢低布局。",
      tags: ["BTC", "技术分析", "牛市"],
      stats: { likes: 234, comments: 56, shares: 12 },
      time: "2小时前",
      chart: true,
    },
    {
      id: 2,
      author: {
        name: "DeFi研究员",
        avatar: "/avatar2.png",
        badge: "认证KOL",
        followers: "8.2K",
      },
      content: "新项目分析：Layer2赛道又有新动作，zkSync生态项目值得关注。TVL持续增长，生态激励计划即将启动。",
      tags: ["Layer2", "DeFi", "zkSync"],
      stats: { likes: 156, comments: 34, shares: 8 },
      time: "4小时前",
    },
    {
      id: 3,
      author: {
        name: "量化交易团队",
        avatar: "/avatar3.png",
        badge: "机构认证",
        followers: "25.6K",
      },
      content: "今日策略：ETH/BTC汇率触及支撑，可考虑配置ETH。网格交易设置：2200-2400区间，每格2%利润。",
      tags: ["ETH", "量化", "网格交易"],
      stats: { likes: 412, comments: 89, shares: 23 },
      time: "6小时前",
      chart: true,
    },
  ];

  const traders = [
    { name: "顶级交易员A", winRate: "78%", pnl: "+12,345", followers: "5.6K", rank: 1 },
    { name: "稳健投资者", winRate: "65%", pnl: "+8,234", followers: "3.2K", rank: 2 },
    { name: "波段高手", winRate: "72%", pnl: "+6,789", followers: "2.8K", rank: 3 },
  ];

  const strategies = [
    { title: "BTC网格策略", type: "网格", profit: "+23.5%", users: "1.2K" },
    { title: "ETH定投计划", type: "定投", profit: "+15.8%", users: "856" },
    { title: "合约对冲套利", type: "套利", profit: "+8.2%", users: "423" },
  ];

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <h1 className="text-3xl font-bold">交易广场</h1>
        <p className="text-muted-foreground mt-2">发现优质交易员，学习交易策略</p>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="mb-6">
          <TabsTrigger value="discover">发现</TabsTrigger>
          <TabsTrigger value="follow">关注</TabsTrigger>
          <TabsTrigger value="copy">跟单</TabsTrigger>
          <TabsTrigger value="strategy">策略</TabsTrigger>
          <TabsTrigger value="learn">学院</TabsTrigger>
        </TabsList>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          <div className="lg:col-span-8">
            <TabsContent value="discover" className="space-y-4">
              {posts.map((post) => (
                <Card key={post.id}>
                  <CardHeader>
                    <div className="flex items-start justify-between">
                      <div className="flex items-center gap-3">
                        <Avatar>
                          <AvatarImage src={post.author.avatar} />
                          <AvatarFallback>{post.author.name[0]}</AvatarFallback>
                        </Avatar>
                        <div>
                          <div className="flex items-center gap-2">
                            <h3 className="font-semibold">{post.author.name}</h3>
                            <Badge variant="secondary" className="text-xs">
                              {post.author.badge}
                            </Badge>
                          </div>
                          <p className="text-sm text-muted-foreground">
                            {post.author.followers} 粉丝 · {post.time}
                          </p>
                        </div>
                      </div>
                      <Button variant="outline" size="sm">
                        <Users className="w-4 h-4 mr-1" />
                        关注
                      </Button>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <p className="mb-3">{post.content}</p>
                    {post.chart && (
                      <div className="bg-muted rounded p-4 mb-3">
                        <div className="h-32 flex items-center justify-center text-muted-foreground">
                          <BarChart3 className="w-8 h-8" />
                          <span className="ml-2">价格走势图</span>
                        </div>
                      </div>
                    )}
                    <div className="flex gap-2 mb-3">
                      {post.tags.map((tag) => (
                        <Badge key={tag} variant="outline">
                          #{tag}
                        </Badge>
                      ))}
                    </div>
                  </CardContent>
                  <CardFooter>
                    <div className="flex gap-4 w-full">
                      <Button variant="ghost" size="sm" className="flex-1">
                        <Heart className="w-4 h-4 mr-1" />
                        {post.stats.likes}
                      </Button>
                      <Button variant="ghost" size="sm" className="flex-1">
                        <MessageCircle className="w-4 h-4 mr-1" />
                        {post.stats.comments}
                      </Button>
                      <Button variant="ghost" size="sm" className="flex-1">
                        <Share2 className="w-4 h-4 mr-1" />
                        {post.stats.shares}
                      </Button>
                    </div>
                  </CardFooter>
                </Card>
              ))}
            </TabsContent>

            <TabsContent value="copy" className="space-y-4">
              <Card>
                <CardHeader>
                  <CardTitle>跟单交易</CardTitle>
                  <CardDescription>复制专业交易员的交易策略</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {traders.map((trader) => (
                      <div key={trader.rank} className="flex items-center justify-between p-4 border rounded-lg">
                        <div className="flex items-center gap-4">
                          <div className="flex items-center justify-center w-8 h-8 rounded-full bg-primary/10 text-primary font-bold">
                            {trader.rank}
                          </div>
                          <Avatar>
                            <AvatarFallback>{trader.name[0]}</AvatarFallback>
                          </Avatar>
                          <div>
                            <h4 className="font-semibold">{trader.name}</h4>
                            <p className="text-sm text-muted-foreground">{trader.followers} 跟单者</p>
                          </div>
                        </div>
                        <div className="flex items-center gap-6">
                          <div className="text-right">
                            <p className="text-sm text-muted-foreground">胜率</p>
                            <p className="font-semibold text-green-500">{trader.winRate}</p>
                          </div>
                          <div className="text-right">
                            <p className="text-sm text-muted-foreground">收益</p>
                            <p className="font-semibold text-green-500">{trader.pnl} USDT</p>
                          </div>
                          <Button size="sm">
                            <Target className="w-4 h-4 mr-1" />
                            跟单
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="strategy" className="space-y-4">
              <Card>
                <CardHeader>
                  <CardTitle>热门策略</CardTitle>
                  <CardDescription>经过验证的量化交易策略</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="grid gap-4">
                    {strategies.map((strategy) => (
                      <Card key={strategy.title}>
                        <CardContent className="pt-6">
                          <div className="flex items-start justify-between">
                            <div>
                              <h4 className="font-semibold mb-1">{strategy.title}</h4>
                              <div className="flex gap-2 mb-2">
                                <Badge>{strategy.type}</Badge>
                                <Badge variant="outline">{strategy.users} 使用</Badge>
                              </div>
                              <p className="text-sm text-muted-foreground">
                                稳定运行30天，最大回撤5%
                              </p>
                            </div>
                            <div className="text-right">
                              <p className="text-2xl font-bold text-green-500">{strategy.profit}</p>
                              <p className="text-sm text-muted-foreground">月收益率</p>
                              <Button size="sm" className="mt-2">使用策略</Button>
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="learn" className="space-y-4">
              <Card>
                <CardHeader>
                  <CardTitle>交易学院</CardTitle>
                  <CardDescription>从入门到精通的交易课程</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    <div className="border rounded-lg p-4">
                      <div className="flex items-start gap-4">
                        <div className="p-2 bg-primary/10 rounded">
                          <BookOpen className="w-6 h-6 text-primary" />
                        </div>
                        <div className="flex-1">
                          <h4 className="font-semibold mb-1">新手入门指南</h4>
                          <p className="text-sm text-muted-foreground mb-2">
                            了解加密货币基础知识，学习如何进行首次交易
                          </p>
                          <div className="flex items-center gap-4 text-sm">
                            <span>12节课</span>
                            <span>·</span>
                            <span>2.5小时</span>
                            <span>·</span>
                            <span>8.5K学员</span>
                          </div>
                        </div>
                        <Button variant="outline">开始学习</Button>
                      </div>
                    </div>
                    <div className="border rounded-lg p-4">
                      <div className="flex items-start gap-4">
                        <div className="p-2 bg-primary/10 rounded">
                          <TrendingUp className="w-6 h-6 text-primary" />
                        </div>
                        <div className="flex-1">
                          <h4 className="font-semibold mb-1">技术分析进阶</h4>
                          <p className="text-sm text-muted-foreground mb-2">
                            掌握K线形态、技术指标、趋势分析等专业技能
                          </p>
                          <div className="flex items-center gap-4 text-sm">
                            <span>24节课</span>
                            <span>·</span>
                            <span>6小时</span>
                            <span>·</span>
                            <span>5.2K学员</span>
                          </div>
                        </div>
                        <Button variant="outline">开始学习</Button>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </TabsContent>
          </div>

          <div className="lg:col-span-4 space-y-4">
            <Card>
              <CardHeader>
                <CardTitle className="text-base">热门话题</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-bold text-orange-500">#1</span>
                      <span className="text-sm">BTC突破历史新高</span>
                    </div>
                    <Badge variant="secondary" className="text-xs">12.5K讨论</Badge>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-bold text-gray-500">#2</span>
                      <span className="text-sm">ETH升级进展</span>
                    </div>
                    <Badge variant="secondary" className="text-xs">8.2K讨论</Badge>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-bold text-yellow-600">#3</span>
                      <span className="text-sm">美联储利率决议</span>
                    </div>
                    <Badge variant="secondary" className="text-xs">6.8K讨论</Badge>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="text-base">推荐关注</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {["顶级分析师", "量化大师", "DeFi专家"].map((name, i) => (
                    <div key={i} className="flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <Avatar className="w-8 h-8">
                          <AvatarFallback>{name[0]}</AvatarFallback>
                        </Avatar>
                        <div>
                          <p className="text-sm font-medium">{name}</p>
                          <p className="text-xs text-muted-foreground">{(i + 1) * 3.2}K 粉丝</p>
                        </div>
                      </div>
                      <Button size="sm" variant="outline">关注</Button>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="text-base">交易排行榜</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Trophy className="w-4 h-4 text-yellow-500" />
                      <span className="text-sm">日收益榜</span>
                    </div>
                    <Button variant="ghost" size="sm">查看</Button>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Star className="w-4 h-4 text-blue-500" />
                      <span className="text-sm">周收益榜</span>
                    </div>
                    <Button variant="ghost" size="sm">查看</Button>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Target className="w-4 h-4 text-green-500" />
                      <span className="text-sm">胜率榜</span>
                    </div>
                    <Button variant="ghost" size="sm">查看</Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </Tabs>
    </div>
  );
}