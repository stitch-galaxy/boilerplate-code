'use strict';

(function() {
    angular.module('templates', []);

    angular.module('stitchGalaxy', [
        'ngRoute',
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

    function config($routeProvider) {
        $routeProvider
                .when('/search', {
                    templateUrl: 'partials/search.html',
                    controller: 'SearchCtrl',
                    controllerAs: 'search'
                })
                .when('/login', {
                    templateUrl: 'partials/login.html',
                    controller: 'LoginCtrl',
                    controllerAs: 'login'})
                .otherwise({redirectTo: '/search'});
    }

    angular
            .module('stitchGalaxy')
            .config(['$routeProvider', config]);
})();