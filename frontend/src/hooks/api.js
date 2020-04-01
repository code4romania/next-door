import useFetch from "use-http";
import { useAuthorization } from "./auth";
import { useDispatch } from "react-redux";
import { apiAc } from "../redux/ducks/api";
import { useCallback, useMemo } from "react";

export const useApi = (url, options = {}, needsAuth = true) => {
  const authorization = useAuthorization(needsAuth);
  const dispatch = useDispatch();

  const fetchOptions = useMemo(
    () => ({
      interceptors: {
        request: options => {
          dispatch(
            apiAc.setStatus({
              key: url + options.method,
              loading: true
            })
          );
          options.headers.Authorization = authorization;
          return options;
        },
        response: async res => {
          if (!res.ok) {
            dispatch(
              apiAc.setStatus({
                key: url + options.method,
                error: res.data
              })
            );
          } else {
            dispatch(
              apiAc.setStatus({
                key: url + options.method,
                data: res.data
              })
            );
          }
          return res;
        }
      },
      url: "/",
      ...options
    }),
    [dispatch, options, authorization, url]
  );

  return useFetch(fetchOptions);
};

export const useApiGet = (url, options = {}, needsAuth = true) => {
  const [request, response] = useApi(
    url,
    {
      cacheLife: 60000,
      persist: true,
      method: "GET",
      ...options
    },
    needsAuth
  );

  const makeRequest = useCallback(() => request.get(url), [request, url]);

  return useMemo(() => [makeRequest, response], [makeRequest, response]);
};

export const useApiPost = (url, data, options = {}, needsAuth = true) => {
  const [request, response] = useApi(
    url,
    {
      method: "POST",
      cachePolicy: "no-cache",
      ...options
    },
    needsAuth
  );

  const makeRequest = useCallback(() => request.post(url, data), [
    request,
    url,
    data
  ]);

  return useMemo(() => [makeRequest, response], [makeRequest, response]);
};
