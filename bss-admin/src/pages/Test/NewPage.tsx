import {useModel} from "@umijs/max";
import {Button} from "antd";


export default () => {
  const message = useModel('demo')
  const { count, add, minus } = useModel('counter', (ret) => {
    return {
      count: ret.counter,
      add: ret.increment,
      minus: ret.decrement,
    };
  });
  return (
    <>
      <div>NewPage</div>
      <div>{message}</div>
      <div>counter: {count}</div>
      <div>
        <Button onClick={add}>add</Button>
        <Button onClick={minus}>minus</Button>
      </div>
    </>
  );
}
