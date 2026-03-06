import {RadiusUpleftOutlined, RadiusUprightOutlined} from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'copyleft',
          title: <RadiusUpleftOutlined />,
          href: '',
          blankTarget: true,
        },
        {
          key: 'copyright 2025',
          title: 'copyright 2025',
          href: '',
          blankTarget: true,
        },
        {
          key: 'copyright',
          title: <RadiusUprightOutlined />,
          href: '',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
