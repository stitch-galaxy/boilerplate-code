'use strict';

var app = angular.module('sgManager');

app.controller('LoginCtrl', function ($scope, AuthenticationService) {
  $scope.login = function (credentials) {
    AuthenticationService.login(credentials);
  };
});