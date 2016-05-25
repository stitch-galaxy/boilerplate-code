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

import intl = require('intl');
import intlRu = require('intl/locale-data/jsonp/ru.js');

declare var Intl: any;

addLocaleData([...ru]);

if (Intl) {
    require.ensure(['intl','intl/locale-data/jsonp/ru.js'], function(require) {
       var intlModule = typeof require('intl');
       var intlRuLocaleDataModule = typeof require('intl/locale-data/jsonp/ru.js');
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
