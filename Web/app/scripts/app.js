'use strict';

var app = angular.module('sgManager', ['ngCookies', 'ngResource', 'ngSanitize', 'ngRoute']);

app.config(function ($routeProvider) {
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
});

app.run(function($rootScope, $location, AuthenticationService) {

  var routesThatRequireAuth = ['/home'];
  $rootScope.$on('$routeChangeStart', function(event, next, current) {

    if (_(routesThatRequireAuth).contains($location.path()) && !AuthenticationService.isLoggedIn()) {
      $location.path('/login');
    }
  });

});