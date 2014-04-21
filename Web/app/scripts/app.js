'use strict';

var app = angular.module('sgManager', ['ngCookies', 'ngResource', 'ngSanitize', 'ngRoute', 'base64']);

app.config(function ($routeProvider, $httpProvider) {
  $routeProvider
  .when('/home', {
    templateUrl: 'views/home.html',
    controller: 'HomeCtrl'
  })
  .when('/login', {
    templateUrl: 'views/login.html',
    controller: 'LoginCtrl'
  })
  .otherwise({
    redirectTo: '/home'
  });

  $httpProvider.defaults.useXDomain = true;
  delete $httpProvider.defaults.headers.common['X-Requested-With'];
});

app.run(function($rootScope, $location, AuthenticationService) {

  var routesThatRequireAuth = ['/home'];
  $rootScope.$on('$routeChangeStart', function(event, next, current) {

    if (_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
      $location.path('/login');
    }
  });

});