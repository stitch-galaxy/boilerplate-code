import * as React from "react";
import { FormattedRelative } from "react-intl";


export interface AboutProps { }

export class About extends React.Component<AboutProps, {}> {
    render() {
        let date = new Date(2011, 11, 3);
        return (
            <h1>About component: Modified <FormattedRelative style="best fit" value={date.getTime()}/>!</h1>
        );
    }
}