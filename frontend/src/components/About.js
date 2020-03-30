import React from "react";
import { Link } from "react-router-dom";

const About = () => (
  <section>
    <article>
      <header>
        <p>About next-door!</p>
        <nav>
          <Link to="/home">Home</Link>
          <Link to="/logout">Logout</Link>
        </nav>
      </header>
      <p>Describing next-door</p>
    </article>
  </section>
);

export default React.memo(About);
