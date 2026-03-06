

function AboutSection() {
  return (
    <div className="section" key={"about-section"}>
      <div className={"flex justify-center"}>
        <div className={"flex flex-col"}>
          <h3 className="scroll-m-20 text-2xl font-semibold tracking-tight text-center">
            关于我们
          </h3>
          <p className="max-w-xl leading-7 [&:not(:first-child)]:mt-4">
            作为国际化的区块链安全领军企业，我们致力于为客户提供从威胁发现到威胁防御的一站式安全解决方案。目前，我们的服务已覆盖全球十几个主要国家和地区，成功为上千家知名项目提供专业安全服务，在区块链安全领域树立了行业标杆。
          </p>
        </div>
      </div>
    </div>
  )
}

export default AboutSection;
