import * as React from "react";

export interface InvalidUrlProps {}

export class InvalidUrl extends React.Component<InvalidUrlProps, {}> {
    render() {
        return <h1>Invalid url component!</h1>;
    }
}