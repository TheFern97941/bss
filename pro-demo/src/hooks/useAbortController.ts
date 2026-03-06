import {useEffect} from 'react';

export default function useAbortController(): AbortSignal {

  const controller = new AbortController();
  useEffect(() => {
    return () => {
      controller.abort();
    };
  }, []);

  return controller.signal;
}
