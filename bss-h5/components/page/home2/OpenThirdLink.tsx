"use client"
import {Button} from "@/components/ui/button";

function OpenThirdLink({tLink}: {tLink: string}) {

  return (
    <Button
      className={""}
      variant={"link"}
      onClick={() => {
        window.open(tLink, "_blank");
      }}
    >
      打开
    </Button>
  )
}

export default OpenThirdLink;