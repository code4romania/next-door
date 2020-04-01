import React, { Suspense, lazy } from "react";
import styles from "./App.module.css";
import { Route, Switch } from "react-router-dom";
import PrivateRoute from "./PrivateRoute";
import Logout from "./Logout";

const Home = lazy(() => import("./Home"));
const About = lazy(() => import("./About"));
const Login = lazy(() => import("./Login"));
const Register = lazy(() => import("./Register"));

function App() {
  return (
    <div className={styles.App}>
      <Suspense fallback={<div>Loading...</div>}>
        <Switch>
          <PrivateRoute exact path="/" component={Home} />
          <PrivateRoute exact path="/about" component={About} />
          <PrivateRoute exact path="/logout" component={Logout} />
          <Route exact path="/login" component={Login} />
          <Route exact path="/register" component={Register} />
        </Switch>
      </Suspense>
    </div>
  );
}

export default React.memo(App);
