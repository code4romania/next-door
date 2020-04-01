import { combineReducers } from "redux";
import api from "./ducks/api";
import auth from "./ducks/auth";

export default combineReducers({
  api,
  auth
});
