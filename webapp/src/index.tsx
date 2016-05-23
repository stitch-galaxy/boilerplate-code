import * as React from "react";
import * as ReactDOM from "react-dom";
import { Router, Route, Link } from "react-router";

import { Hello } from "./components/Hello";

ReactDOM.render(
    <Hello compiler="TypeScript" framework="React" />,
    document.getElementById("example")
);