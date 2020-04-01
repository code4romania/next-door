import { createAction, createReducer } from "redux-act";
import { createSelector } from "reselect";
import { apiAc, apiSel } from "./api";
import { authPaths } from "../../hooks/auth";

export const authAc = {
  setJwt: createAction("set jwt"),
  clearLoginError: apiAc.clearError(`${authPaths.login()}POST`),
  clearRegisterError: apiAc.clearError(`${authPaths.register()}POST`)
};

export const JWT_KEY = "jwt";
const existingJwt = localStorage.getItem(JWT_KEY);

const stateSel = state => state.auth;
const jwtSel = createSelector(stateSel, s => s.jwt);
const loginStatusSel = apiSel.statusSelCreator(`${authPaths.login()}POST`);
const registerStatusSel = apiSel.statusSelCreator(
  `${authPaths.register()}POST`
);

export const authSel = {
  jwt: jwtSel,
  loginStatus: loginStatusSel,
  registerStatus: registerStatusSel
};

const reducer = createReducer(
  {
    [authAc.setJwt]: (state, payload) => ({
      ...state,
      jwt: payload
    })
  },
  { jwt: existingJwt }
);

export default reducer;
