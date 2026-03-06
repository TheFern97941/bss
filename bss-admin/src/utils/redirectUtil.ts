import {message} from "antd";
import {history} from "@@/core/history";
import {removeToken} from "@/utils/authUtil";

export const redirectByTimeout = () => {
  message.warning("登录已过期，请重新登录");
  removeToken();
  setTimeout(() => {
    history.replace('/user/login');
  }, 1000);
}
