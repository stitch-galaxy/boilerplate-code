import * as React from "react";
import * as ReactDOM from "react-dom";
import { Provider } from 'react-redux';

import { Router, Route, IndexRedirect, hashHistory } from 'react-router';

import { App } from "./components/App";
import { Gallery } from "./components/Gallery";
import { About } from "./components/About";
import { InvalidUrl } from "./components/InvalidUrl";
import { IntlProvider, addLocaleData } from 'react-intl';
import * as ru from 'react-intl/locale-data/ru';
import * as Redux from 'redux';

import store from './store/store';

import intlPolyfill = require('./intlPolyfill');

declare var Intl: any;

addLocaleData([...ru]);
//Polyfill:
//http://formatjs.io/guides/runtime-environments/
//https://github.com/andyearnshaw/Intl.js
//Issue:
//https://github.com/TypeStrong/ts-loader/issues/26
//http://www.typescriptlang.org/docs/handbook/modules.html
//Examples:
//https://github.com/TypeStrong/ts-loader/tree/master/test/codeSplitting
//https://github.com/webpack/webpack/tree/master/examples/code-splitting
//Web pack feature description
//http://webpack.github.io/docs/code-splitting.html
//General notes on conditional module loading: conditional module loaders such as
//http://yepnopejs.com/
//deprecated and replaced with simple script and automated module bundlers
//https://github.com/SlexAxton/yepnope.js#deprecation-notice
if (Intl) {
    require.ensure(['./intlPolyfill'], function (require) {
        //use this expressions to evaluate module
        //or comment them to just make modules downloaded and availiable for require
        var intlPolyfillModule = typeof require('./intlPolyfill');
        //following line do not work: use tsc -p . to see what code typescript compiler generate
        //var intlPolyfillModule = typeof intlPolyfill;
    });
    runMyApp();
} else {
    runMyApp()
}

function runMyApp() {
    ReactDOM.render(
        <Provider store={store}>
            <IntlProvider locale={navigator.language}>
                <Router history={hashHistory}>
                    <Route path="/" component={App}>
                        <IndexRedirect to="/gallery" />
                        <Route path="gallery" component={Gallery}/>
                        <Route path="about" component={About}/>
                    </Route>
                </Router>
            </IntlProvider>
        </Provider>
        ,
        document.getElementById("root")
    );
}
