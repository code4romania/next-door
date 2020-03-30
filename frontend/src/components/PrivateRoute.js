import React from "react";
import { useSelector } from "react-redux";
import { authSel } from "../redux/ducks/auth";
import { Redirect, Route } from "react-router-dom";
import { isExpired } from "../api/auth";

const PrivateRoute = props => {
  const token = useSelector(authSel.jwt);
  const isAuthenticated = !isExpired(token);

  if (!isAuthenticated) {
    return <Redirect to="/login" />;
  }

  return <Route {...props} />;
};

export default React.memo(PrivateRoute);
