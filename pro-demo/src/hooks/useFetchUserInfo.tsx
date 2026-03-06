import { flushSync } from 'react-dom';
import {useModelInitState} from "@/hooks/useModelInitState";

export const useFetchUserInfo = () => {
  const { initialState, setInitialState } = useModelInitState();

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      flushSync(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  return fetchUserInfo;
};
