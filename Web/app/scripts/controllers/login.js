'use strict';

var app = angular.module('sgManager');

app.controller('LoginCtrl', function ($scope, $location, AuthenticationService) {
  $scope.LoginBtnDisabled = false;
  $scope.login = function (credentials) {
    $scope.LoginBtnDisabled = true;
    var promise = AuthenticationService.login(credentials);
    promise.success(function() {
      $location.path('/home');
    });
    promise.then(function() {
      $scope.LoginBtnDisabled = false;
    });

  };
});