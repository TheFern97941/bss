import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";

interface ServiceDesc {
  icon: string;
  title: string;
  descList: string[];
}

const ServiceDescList: ServiceDesc[] = [
  {
    icon: "/home/sps/acid-shield.svg",
    title: "隐私钱包",
    descList: [
      "多重签名技术保障资产安全",
      "零知识证明保护交易隐私",
      "去中心化身份验证系统",
      "跨链资产统一管理"
    ]
  },
  {
    icon: "/home/sps/sell-card.svg",
    title: "隐私支付",
    descList: [
      "环签名技术隐藏交易来源",
      "混币服务增强匿名性",
      "即时支付确认机制",
      "低手续费高安全性保障"
    ]
  },
  {
    icon: "/home/sps/andromeda-chain.svg",
    title: "链上安全",
    descList: [
      "智能合约安全审计",
      "漏洞检测与修复服务",
      "实时安全监控预警",
      "应急响应与恢复方案"
    ]
  },
  {
    icon: "/home/sps/thorn-helix.svg",
    title: "链上审计",
    descList: [
      "代码质量深度分析",
      "合规性检查与报告",
      "性能优化建议",
      "安全最佳实践指导"
    ]
  },
]

function ServiceSection() {
  return (
    <div className="section" key={"service-section"}>
      <h3 className="scroll-m-20 text-2xl font-semibold tracking-tight text-center">
        解决核心安全问题的产品才是我们要创造的
      </h3>
      <p className="leading-7 [&:not(:first-child)]:mt-2 text-center">
        从 &#34;威胁发现&#34; 到 &#34;威胁防御&#34; 完整安全服务方案
      </p>

      <div className="flex justify-center mt-6">
        <div className={"inline-grid grid-cols-2 gap-4"}>
          {
            ServiceDescList.map((sd, index) => (
              <Card key={index} className={"min-w-sm flex flex-row items-center gap-0"}>
                <div className={"flex justify-center ml-6"}>
                  <img
                    className={"w-24 h-24"}
                    src={sd.icon}
                  />
                </div>
                <div className={"flex-1"}>
                  <CardHeader>
                    <CardTitle>{sd.title}</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <ul className="my-6 ml-6 list-disc [&>li]:mt-2">
                      {
                        sd.descList.map((v, i) => (
                          <li className={"text-muted-foreground text-sm"} key={i}>{v}</li>
                        ))
                      }
                    </ul>
                  </CardContent>
                </div>
              </Card>
            ))
          }
        </div>
      </div>
    </div>
  )
}

export default ServiceSection