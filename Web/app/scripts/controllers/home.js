'use strict';

var app = angular.module('sgManager');

app.controller('HomeCtrl', function ($scope, AuthenticationService) {
  $scope.logout = function () {
    AuthenticationService.logout();
  };
});

