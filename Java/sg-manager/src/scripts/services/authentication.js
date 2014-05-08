'use strict';

var app = angular.module('sgManager');


app.factory('SessionService', function(){
  return {
    getCredentials: function()
    {
      return sessionStorage.getItem('credentials');
    },
    setCredentials: function(val) {
      return sessionStorage.setItem('credentials', val);
    },
    removeCredentials: function() {
      return sessionStorage.remove('credentials');
    }
  };
});

app.factory('AuthenticationService', function($http, $location, $base64, SessionService) {
  return {
    login: function(credentials) {

      var login = $http({method: 'GET', url: 'https://localhost/test/login', headers: {'Authorization': 'Basic ' + $base64.encode(credentials.email + ':' + credentials.password)}});

      login.success(function() {
        SessionService.setCredentials(credentials);
      });
      return login;
    },
    logout: function() {
      SessionService.removeCredentials();
    },
    isLoggedIn: function() {
      return SessionService.getCredentials();
    }
  };
});