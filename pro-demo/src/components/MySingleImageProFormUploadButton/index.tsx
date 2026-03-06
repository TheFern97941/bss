import { getToken } from '@/utils/authUtil';
import { Result } from '@/requestErrorConfig';
import {message, UploadFile} from 'antd';
import { redirectByTimeout } from '@/utils/redirectUtil';
import { ProFormUploadButton } from '@ant-design/pro-form';
import { ProFormUploadButtonProps } from '@ant-design/pro-form/es/components/UploadButton';

export function convertImageToUploadFormat(url?: string | null, name = '图片'): UploadFile[] {
  if (!url) return [];
  return [
    {
      uid: url,
      url,
      name,
      status: 'done',
    },
  ];
}


interface MySingleImageProFormUploadButtonProps extends ProFormUploadButtonProps {}

const MySingleImageProFormUploadButton = (props: MySingleImageProFormUploadButtonProps) => {
  const {name, ...rest} = props;

  return (
    <ProFormUploadButton
      width="md"
      name={name}
      max={1}
      accept="image/*"
      action={'/api/fileUpload'}
      fieldProps={{
        headers: { Authorization: getToken() || '' },
      }}
      transform={(value) => {
        console.log('transform: ', value);
        return {
          [name]: value?.[0]?.response?.data,
        };
      }}
      onChange={(info) => {
        if (info.file.status === 'done') {
          console.log('图片上传结束'); // 后端返回的路径
          const res = info.file.response as unknown as Result<string>;
          if (!res.success) {
            console.log('图片上传结束');

            if (res.errorMessage) message.error(res.errorMessage);
            if (res.errorCode === 'token.timeOut') {
              redirectByTimeout();
            }
          } else {
            message.success("上传成功!");
          }
        }
      }}
      {...rest}
    />
  );
}

export default MySingleImageProFormUploadButton
