import { useDispatch, useSelector } from "react-redux";
import { authAc, authSel, JWT_KEY } from "../redux/ducks/auth";
import { useEffect, useMemo } from "react";
import { useApiPost } from "./api";
import { useHistory } from "react-router-dom";
import { isExpired } from "../api/auth";

export const authPaths = {
  register: () => "api/authentication/register",
  login: () => "api/authentication/login"
};

export const useSignup = user => {
  const [makeRequest, response] = useApiPost(
    authPaths.register(),
    user,
    {},
    false
  );
  const history = useHistory();

  useEffect(() => {
    if (response.ok) {
      history.push("/login");
    }
  }, [response, history]);

  return makeRequest;
};

export const useSignin = user => {
  const [makeRequest, response] = useApiPost(
    authPaths.login(),
    user,
    {},
    false
  );
  const dispatch = useDispatch();
  const history = useHistory();

  useEffect(() => {
    if (response.ok && response.data) {
      const { accessToken } = response.data;
      localStorage.setItem(JWT_KEY, accessToken);
      dispatch(authAc.setJwt(accessToken));

      history.push("/");
    }
  }, [response, history, dispatch]);

  return makeRequest;
};

export const useAuthorization = (needsToken = true) => {
  const token = useSelector(authSel.jwt);
  const history = useHistory();

  useEffect(() => {
    if (isExpired(token) && needsToken) {
      // we should try and refresh the token here
      history.push("/login");
    }
  }, [history, token, needsToken]);

  return useMemo(() => `Bearer ${token}`, [token]);
};

export const useLogout = () => {
  const dispatch = useDispatch();
  useEffect(() => {
    localStorage.removeItem(JWT_KEY);
    dispatch(authAc.setJwt(undefined));
  }, [dispatch]);
};
