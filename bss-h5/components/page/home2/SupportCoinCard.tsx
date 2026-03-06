import {Coin} from "@/api/model/coin/Coin";
import {Card, CardAction, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Table, TableBody, TableCell, TableRow} from "@/components/ui/table";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import Link from "next/link";

function SupportCoinCard({supportCoins}: { supportCoins: Coin[] }) {
  return (
    <Card className="w-full min-w-sm">
      <CardHeader>
        <CardTitle>支持币种</CardTitle>
        <CardAction>
          <Link href={""}>更多 &gt;</Link>
        </CardAction>
      </CardHeader>
      <CardContent>
        <Table>
          <TableBody>
            {supportCoins.map((coin, index) => (
              <TableRow key={coin.id}>
                <TableCell className="flex items-center gap-2">
                  <Avatar className={"w-6 h-6"}>
                    <AvatarImage src={coin.icon}/>
                    <AvatarFallback>{coin.coinName}</AvatarFallback>
                  </Avatar>
                  <span>{coin.symbol}</span>
                  <span className="font-light text-secondary">
                   {coin.coinName}
                  </span>
                </TableCell>
                <TableCell className="text-right">
                  1234
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  )
}

export default SupportCoinCard;
