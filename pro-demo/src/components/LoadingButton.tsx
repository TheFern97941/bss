import React, {useState} from 'react';
import {Button} from 'antd';
import type {ButtonProps} from 'antd/lib/button/button';


type LoadingButtonProps = Omit<ButtonProps, 'onClick'> & {
  // onClick?: Promise<void>
  onClick?: (e: React.MouseEvent<HTMLElement, MouseEvent>) => Promise<void>
}

const LoadingButton: React.FC<LoadingButtonProps> = (props: LoadingButtonProps) => {
  const {onClick} = props;
  const [loading, setLoading] = useState(false);

  async function doOnClick(e: React.MouseEvent<HTMLElement, MouseEvent>) {
    if (!onClick) {
      return;
    }
    setLoading(true);
    try {
      await onClick(e);
    } finally {
      setLoading(false);
    }
  }

  return <Button {...props} onClick={(e)=>doOnClick(e)} loading={loading}/>;
};

export default LoadingButton;
