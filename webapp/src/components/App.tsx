import * as React from "react";
import { Link } from 'react-router'

export interface AppProps { compiler: string; framework: string; }

export class App extends React.Component<AppProps, {}> {
    render() {
        return (
            <div>
                <h1>Stitch galaxy application</h1>
                <ul>
                    <li><Link to="/about">About</Link></li>
                    <li><Link to="/gallery">Gallery</Link></li>
                </ul>
                {this.props.children}
            </div>
        );
    }
}