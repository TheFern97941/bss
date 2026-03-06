// import {Tooltip} from "antd";
// import React from "react";
// import {BaseDateTimeTemplate, toDayjs} from "@/utils/dateUtils";
// // import {useCurrentTimeZone} from "@/hooks/TimeZoneContext";
//
// export interface TimeZoneFieldProps {
//   date: any;
// }
//
// export default function TimeZoneField({date}: TimeZoneFieldProps) {
//   const timeZone = useCurrentTimeZone();
//
//   if (!date) return <></>;
//   const dateDayjs = toDayjs(date);
//
//   return (
//     <>
//       {
//         timeZone && <Tooltip title={`北京时间:${dateDayjs.format(BaseDateTimeTemplate)}`}>
//           {
//             dateDayjs
//               .tz(timeZone.id)
//               .format(BaseDateTimeTemplate)
//           }
//         </Tooltip>
//       }
//     </>
//   );
// }
