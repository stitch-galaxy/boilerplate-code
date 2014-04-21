'use strict';

var app = angular.module('sgManager');

app.controller('LoginCtrl', function ($scope, $location, AuthenticationService) {
  $scope.login = function (credentials) {
    AuthenticationService.login(credentials).success(function() {
      $location.path('/home');
    });
  };
});