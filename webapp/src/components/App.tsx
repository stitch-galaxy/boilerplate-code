import * as React from "react";
import { Link } from 'react-router'

export interface AppProps { compiler: string; framework: string; app: any; }

export class App extends React.Component<AppProps, {}> {
    constructor(props: AppProps, context: any) {
        super(props);
        this.state = {
            name: props.app.greeting
        };
    }

    render() {
        return (
            <div>
                <h1>{name}</h1>
                <ul>
                    <li><Link to="/about">About</Link></li>
                    <li><Link to="/gallery">Gallery</Link></li>
                </ul>
                {this.props.children}
            </div>
        );
    }
}