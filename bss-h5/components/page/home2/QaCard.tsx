import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Table, TableBody, TableCell, TableRow} from "@/components/ui/table";

function QaCard() {
  return (
    <Card className="w-full min-w-sm">
      <CardHeader>
        <CardTitle>常见问题</CardTitle>
      </CardHeader>
      <CardContent>
        <Table>
          <TableBody>
            {
              [
                ["🌐 网络是否安全?", "仅使用加密通讯"],
                ["🌐 资金是否安全?", "7*24服务,丢失包赔"],
                ["🌐 如何保证客户隐私?", "全程匿名服务"],
                ["🌐 是否支持三方上押?", "支持"],
                ["🌐 是否回头币?", "回头币10倍赔偿"],
              ].map(([q, a], i) => (
                <TableRow key={i}>
                  <TableCell className="font-medium">{q}</TableCell>
                  <TableCell className="font-light">{a}</TableCell>
                </TableRow>
              ))
            }
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  )
}

export default QaCard;