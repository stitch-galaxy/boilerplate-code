'use strict';

(function () {
    angular.module('templates', []);

    angular.module('stitchGalaxy', [
        'ui.router',
        'templates',
        'pascalprecht.translate'
    ]);

    //Search controller
    function SearchCtrl() {
    }

    angular
            .module('stitchGalaxy')
            .controller('SearchCtrl', SearchCtrl);

    //Login controller
    function LoginCtrl() {
    }

    angular
            .module('stitchGalaxy')
            .controller('LoginCtrl', LoginCtrl);


    //Login controller
    function AppCtrl($rootScope, $stateParams, $translate) {
        //https://github.com/angular-ui/ui-router/issues/1307
        $rootScope.activeLang = $stateParams.lang;
        $translate.use($rootScope.activeLang);

        $rootScope.$on('$stateChangeSuccess', function rootStateChangeSuccess(event, toState, toParams, fromState, fromParams) {
            if ($stateParams.lang !== undefined) {
                $rootScope.activeLang = toParams.lang;
                $translate.use($rootScope.activeLang);
            }
        });
    }

    angular
            .module('stitchGalaxy')
            .controller('AppCtrl', ['$rootScope', '$stateParams', '$translate', AppCtrl]);

    function config($stateProvider, $urlRouterProvider, $translateProvider) {
        var enTranslations = {
            TEXT: 'English text'
        };
        var ruTranslations = {
            TEXT: 'Russian text'
        };

        $translateProvider
                .translations('en', enTranslations)
                .translations('ru', ruTranslations)
                .preferredLanguage('en');

        //
        // For any unmatched url, redirect to /state1
        $urlRouterProvider.otherwise('/en/search');

        $stateProvider
                .state('app', {
                    abstract: true,
                    url: '/{lang:(?:en|ru)}',
                    template: '<ui-view/>',
                    controller: 'AppCtrl',
                    controllerAs: 'app'
                })
                .state('app.search', {
                    url: '/search',
                    templateUrl: 'partials/search.html',
                    controller: 'SearchCtrl',
                    controllerAs: 'search'
                })
                .state('app.login', {
                    url: '/login',
                    templateUrl: 'partials/login.html',
                    controller: 'LoginCtrl',
                    controllerAs: 'login'});
    }

    angular
            .module('stitchGalaxy')
            .config(['$stateProvider', '$urlRouterProvider', '$translateProvider', config]);
})();