import {Card, CardContent} from "@/components/ui/card";
import IndexContainer from "@/components/page/home/IndexContainer";
import {getSupportCoins} from "@/api/services/CoinService";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";

async function IndexSupport() {
  const supportCoins = await getSupportCoins()

  return (
    <IndexContainer>
      <Card className="my-10 rounded-4xl">
        <CardContent>
          <div className="flex border-b">
            <div className="border-r p-2 flex-1 h-12"></div>
            <div className="p-2 flex-3"></div>
          </div>
          <TableBodyRow>
            <TableBodyLeftCell>
              商家支持的链
            </TableBodyLeftCell>
            <TableBodyRightCell myClassName={"space-x-6 py-8"}>
              {
                supportCoins.map((coin, index) => (
                  <Avatar key={index} className={"w-8 h-8"}>
                    <AvatarImage src={coin.icon}/>
                    <AvatarFallback>{coin.coinName}</AvatarFallback>
                  </Avatar>
                ))
              }
            </TableBodyRightCell>
          </TableBodyRow>
          <TableBodyDownSvgRow />
          <TableBodyRow>
            <TableBodyLeftCell></TableBodyLeftCell>
            <TableBodyRightCell myClassName={"space-x-6 py-8"}>
              Tirx (Coin Mixer)
            </TableBodyRightCell>
          </TableBodyRow>
          <TableBodyDownSvgRow />
          <TableBodyRow>
            <TableBodyLeftCell>
              您需要的币
            </TableBodyLeftCell>
            <TableBodyRightCell myClassName={"space-x-6 py-8"}>
              <div className="relative flex size-3">
                <span
                  className="absolute inline-flex h-full w-full animate-ping rounded-full bg-primary opacity-75"></span>
                <span
                  className="relative inline-flex size-3 rounded-full bg-primary"></span>
              </div>
              <span>交易所支持的任意币</span>
              <div className="relative flex size-3">
                <span
                  className="absolute inline-flex h-full w-full animate-ping rounded-full bg-primary opacity-75"></span>
                <span
                  className="relative inline-flex size-3 rounded-full bg-primary"></span>
              </div>
            </TableBodyRightCell>
          </TableBodyRow>
        </CardContent>
      </Card>
    </IndexContainer>
  )
}

function TableBodyRow({children}: { children?: React.ReactNode }) {
  return (
    <div className="flex">{children}</div>
  )
}

function TableBodyLeftCell({children}: { children?: React.ReactNode }) {
  return (
    <div className="border-r p-2 flex-1 flex justify-center items-center min-h-12">{children}</div>
  )
}

function TableBodyRightCell({children, myClassName}: { children?: React.ReactNode,myClassName?: string }) {
  return (
    <div className={`p-2 flex-3 flex justify-center items-center min-h-12 ${myClassName}`}>{children}</div>
  )
}

function TableBodyDownSvgRow() {
  return (
    <TableBodyRow>
      <TableBodyLeftCell></TableBodyLeftCell>
      <TableBodyRightCell myClassName={"space-x-6 py-8"}>
        <svg
          className="w-10 h-10 text-primary animate-bounce"
          viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4094">
          <path
            d="M866.56 447.296L544 770.48V96h-80v672.16L142.448 447.296 85.248 503.84 503.84 922.448 922.496 503.84z"
            fill="currentColor"></path>
        </svg>
      </TableBodyRightCell>
    </TableBodyRow>
  )
}

export default IndexSupport;