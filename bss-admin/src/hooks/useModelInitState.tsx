import {useModel} from "@umijs/max";

export const useModelInitState = () => {
  return useModel("@@initialState")
}

