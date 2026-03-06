// import {Select} from 'antd';
// import {TimeZoneData, useChangeTimeZone, useCurrentTimeZone} from "@/hooks/TimeZoneContext";
//
//
// const {Option} = Select;
//
// export const TimeZoneSelect: React.FC = () => {
//   const changeTimeZone = useChangeTimeZone();
//   const timeZone = useCurrentTimeZone();
//
//   return timeZone && (
//     <Select
//       value={timeZone.id}
//       style={{width: 200}}
//       onChange={(value) => {
//         const selectedTimeZone = TimeZoneData.find(tz => tz.id === value);
//         if (selectedTimeZone) {
//           changeTimeZone(selectedTimeZone);
//         }
//       }}
//     >
//       {TimeZoneData.map(tz => (
//         <Option key={tz.id} value={tz.id}>
//           {tz.name} {tz.offset}
//         </Option>
//       ))}
//     </Select>
//   );
// };
//
// export default TimeZoneSelect;
