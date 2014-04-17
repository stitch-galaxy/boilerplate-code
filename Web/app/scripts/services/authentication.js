'use strict';

var app = angular.module('sgManager');

app.factory('AuthenticationService', function($location) {
  return {
    login: function(credentials) {
      credentials.userName = 'test';
      $location.path('/home');
    },
    logout: function()
    {
      $location.path('/login');
    }
  };
});