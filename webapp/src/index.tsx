import * as React from "react";
import * as ReactDOM from "react-dom";
import * as Redux from 'redux';
import { Provider } from 'react-redux';
import store from './store/store';

import {Root} from './components/Root';
import { setupI18n } from './i18n/i18n';


setupI18n();
runMyApp();

function runMyApp() {
    ReactDOM.render(
        <Provider store={store}>
            <Root/>
        </Provider>
        ,
        document.getElementById("root")
    );
}
