'use strict';

(function () {
    angular.module('templates', []);

    angular.module('stitchGalaxy', [
        'ui.router',
        'templates',
        'pascalprecht.translate',
        'ngSanitize'
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

    //Langage controller
    function LanguageCtrl($scope, $translate) {
        $translate(['EN', 'RU']).then(function (translations) {
            $scope.languages = [];
            $scope.languages.push({locale: 'ru',
                native: 'Русский',
                foreign: translations.RU
            });
            $scope.languages.push({locale: 'en',
                native: 'English',
                foreign: translations.EN
            });
            
            $scope.languages.sort(function(a,b) {return (a.foreign > b.foreign) ? 1 : ((b.foreign > a.foreign) ? -1 : 0);});
        });
    }

    angular
            .module('stitchGalaxy')
            .controller('LanguageCtrl', ['$scope', '$translate', LanguageCtrl]);


    //App controller
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
        $translateProvider
                .useStaticFilesLoader({
                    prefix: '/assets/translations/locale-',
                    suffix: '.json'
                })
                //https://github.com/angular-translate/angular-translate/issues/1101
                .useSanitizeValueStrategy('sanitizeParameters')
                .fallbackLanguage('en')
                .preferredLanguage('en');

        //
        // For any unmatched url, redirect to /state1
        $urlRouterProvider.otherwise('/en/search');

        $stateProvider
                .state('app', {
                    abstract: true,
                    url: '/{lang:(?:en|ru)}',
                    template: '<div ui-view class="site__body"></div>',
                    controller: 'AppCtrl',
                    controllerAs: 'app'
                })
                .state('app.search', {
                    url: '/search',
                    templateUrl: 'partials/search.html',
                    controller: 'SearchCtrl',
                    controllerAs: 'search'
                })
                .state('app.language', {
                    url: '/language',
                    templateUrl: 'partials/language.html',
                    controller: 'LanguageCtrl',
                    controllerAs: 'language'
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