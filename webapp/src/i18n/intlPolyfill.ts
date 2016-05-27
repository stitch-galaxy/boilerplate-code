import intl = require('intl');
import intlRu = require('intl/locale-data/jsonp/ru.js');

//Better solution - because webpack would not drop unused variables
var intlModule = typeof require('intl');
//var intlModule = typeof intl;
//Will work even if webpack will drop unused variables because import intl = require('intl') will be compiled to var intl = require('intl');
var intlRuLocaleDataModule = typeof require('intl/locale-data/jsonp/ru.js');

console.log('Intl polyfill evaluated');

export = 'intlPolyfill';