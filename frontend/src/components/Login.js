import React, { useEffect, useState } from "react";
import { useSignin } from "../hooks/auth";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { authAc, authSel } from "../redux/ducks/auth";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const doLogin = useSignin({ email, password });
  const { error, loading } = useSelector(authSel.loginStatus);
  const dispatch = useDispatch();

  const login = e => {
    e.preventDefault();
    doLogin();
  };

  const emailChange = e => setEmail(e.target.value);
  const passwordChange = e => setPassword(e.target.value);

  useEffect(() => {
    dispatch(authAc.clearLoginError);
  }, [dispatch]);

  return (
    <section>
      <form onSubmit={login}>
        <input
          type="text"
          required
          disabled={loading}
          autoComplete="username"
          value={email}
          onChange={emailChange}
        />
        <input
          type="password"
          required
          disabled={loading}
          autoComplete="current-password"
          value={password}
          onChange={passwordChange}
        />
        <input type="submit" disabled={loading} value="Login" />
      </form>
      <div>{error && error.additionalInfo}</div>
      <Link to="/register">Register</Link>
    </section>
  );
};

export default React.memo(Login);
