import * as React from "react";
import * as ReactDOM from "react-dom";
import { Provider } from 'react-redux';

import { Router, Route, IndexRedirect, hashHistory } from 'react-router';

import { App } from "./App";
import { Gallery } from "./Gallery";
import { About } from "./About";
import { InvalidUrl } from "./InvalidUrl";
import { IntlProvider, addLocaleData } from 'react-intl';
import * as ru from 'react-intl/locale-data/ru';
import * as Redux from 'redux';
import { syncHistoryWithStore } from 'react-router-redux';
import store from '../store/store';

export interface RootProps { }

const history = syncHistoryWithStore(hashHistory, store)

export class Root extends React.Component<RootProps, {}> {
    render() {
        return (
            <div>
                <IntlProvider locale={navigator.language}>
                    <Router history={hashHistory}>
                        <Route path="/" component={App}>
                            <IndexRedirect to="/gallery" />
                            <Route path="gallery" component={Gallery}/>
                            <Route path="about" component={About}/>
                        </Route>
                    </Router>
                </IntlProvider>
                { this.renderDevTools() }
            </div>
        );
    }

    renderDevTools() {
        const rootChildren: any = [
        ];
        if (__DEVTOOLS__) {
            const DevTools = require<ModuleInterface>("./DevTools").default;
            rootChildren.push(<DevTools key="devtools" />);
        }
        return rootChildren;
    }
}