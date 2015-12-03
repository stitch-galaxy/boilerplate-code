'use strict';

(function () {
    angular.module('templates', []);

    angular.module('stitchGalaxy', [
        'ui.router',
        'templates'
    ]);

    //Search controller
    function SearchCtrl() {
    }

    angular
            .module('stitchGalaxy')
            .controller('SearchCtrl', SearchCtrl);

    //Search controller
    function LoginCtrl() {

    }

    angular
            .module('stitchGalaxy')
            .controller('LoginCtrl', LoginCtrl);

    function config($stateProvider, $urlRouterProvider) {
        //
        // For any unmatched url, redirect to /state1
        $urlRouterProvider.otherwise('/search');

        $stateProvider
                .state('search', {
                    url: '/search',
                    templateUrl: 'partials/search.html',
                    controller: 'SearchCtrl',
                    controllerAs: 'search'
                })
                .state('login', {
                    url: '/login',
                    templateUrl: 'partials/login.html',
                    controller: 'LoginCtrl',
                    controllerAs: 'login'});
    }

    angular
            .module('stitchGalaxy')
            .config(['$stateProvider', '$urlRouterProvider', config]);
})();