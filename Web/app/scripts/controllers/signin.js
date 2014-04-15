'use strict';

angular.module('webApp')
.controller('SigninCtrl', function ($scope, $location) {
  $scope.signin = function (user) {
    $location.path('/');
  };
});