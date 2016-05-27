import { addLocaleData } from 'react-intl';
import * as ru from 'react-intl/locale-data/ru';

import intlPolyfill = require('./intlPolyfill');

declare var Intl: any;

export function setupI18n(): void {
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
    if (!Intl) {
        require.ensure(['./intlPolyfill'], function (require) {
            //use this expressions to evaluate module
            //or comment them to just make modules downloaded and availiable for require
            var intlPolyfillModule = typeof require('./intlPolyfill');
            //following line do not work: use tsc -p . to see what code typescript compiler generate
            //var intlPolyfillModule = typeof intlPolyfill;
        });

    }
}