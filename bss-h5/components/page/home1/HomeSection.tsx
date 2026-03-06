
function HomeSection() {
  return (
    <div className="section" key={"home-section"}>
      <div className="h-screen bg-[url(/home/home-bg.jpg)] bg-cover flex flex-col justify-center items-center">
        <div className={"flex flex-col gap-8"}>
          <div>
            <h1 key={"t1"} className="scroll-m-20 text-center text-4xl font-extrabold tracking-tight text-balance">
              构建属于未来的隐私
            </h1>
            <h1 key={"t2"} className="scroll-m-20 text-center text-4xl font-extrabold tracking-tight text-balance mt-2">
              与安全基石
            </h1>
          </div>
          <h2 className={"text-muted-foreground text-xl text-center"}>
            隐私支付 · 链上安全 · 专业审计
          </h2>
        </div>
      </div>
    </div>
  )
}

export default HomeSection;