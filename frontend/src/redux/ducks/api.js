import { createAction, createReducer } from "redux-act";
import { createSelector } from "reselect";

export const apiAc = {
  setStatus: createAction("set api request status"),
  clearError: createAction("clear api request error")
};

const stateSel = state => state.api;
const statusSelCreator = key => createSelector(stateSel, s => s[key] || {});

export const apiSel = {
  state: stateSel,
  statusSelCreator
};

const reducer = createReducer(
  {
    [apiAc.setStatus]: (state, payload) => ({
      ...state,
      [payload.key]: payload
    }),
    [apiAc.clearError]: (state, payload) => ({
      ...state,
      [payload]: undefined
    })
  },
  {}
);

export default reducer;
