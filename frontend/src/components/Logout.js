import { useLogout } from "../hooks/auth";
import React from "react";

const Logout = () => {
  useLogout();
  return null;
};

export default React.memo(Logout);
