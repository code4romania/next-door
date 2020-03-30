import React, { useEffect, useState } from "react";
import { useSignup } from "../hooks/auth";
import { Link } from "react-router-dom";
import { authAc } from "../redux/ducks/auth";
import { useDispatch } from "react-redux";

const Register = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const doRegister = useSignup({ email, password });
  const dispatch = useDispatch();

  const register = e => {
    e.preventDefault();
    doRegister();
  };

  const emailChange = e => setEmail(e.target.value);
  const passwordChange = e => setPassword(e.target.value);

  useEffect(() => {
    dispatch(authAc.clearRegisterError);
  }, [dispatch]);

  return (
    <section>
      <form onSubmit={register}>
        <input
          type="text"
          required
          autoComplete="username"
          value={email}
          onChange={emailChange}
        />
        <input
          type="password"
          required
          autoComplete="new-password"
          value={password}
          onChange={passwordChange}
        />
        <input type="submit" value="Register" />
      </form>
      <Link to="/login">Login</Link>
    </section>
  );
};

export default React.memo(Register);
