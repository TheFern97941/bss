import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
import timezone from "dayjs/plugin/timezone";
import relativeTime from "dayjs/plugin/relativeTime";
import duration from "dayjs/plugin/duration";
import updateLocale from "dayjs/plugin/updateLocale";

dayjs.extend(utc);
dayjs.extend(timezone);
dayjs.extend(relativeTime)
dayjs.extend(duration)
dayjs.locale('zh-cn')
dayjs.tz.setDefault("Asia/Shanghai");
dayjs.extend(updateLocale);
dayjs.updateLocale('zh-cn', {
  weekStart: 1,
});
