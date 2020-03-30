import React from "react";
import { Link } from "react-router-dom";

const Home = () => (
  <section>
    <header>
      <p>Welcome to next door app...</p>
      <nav>
        <Link to="/about">About</Link>
        <Link to="/logout">Logout</Link>
      </nav>
    </header>
    <article>
      <header>
        <h1>Welcome home!</h1>
      </header>
      <p>This is the home of next-door</p>
    </article>
  </section>
);

export default React.memo(Home);
