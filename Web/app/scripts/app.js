'use strict';

angular
.module('sgManager', ['ngCookies', 'ngResource', 'ngSanitize', 'ngRoute'])
.config(function ($routeProvider) {
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
    redirectTo: '/login'
  });
});
