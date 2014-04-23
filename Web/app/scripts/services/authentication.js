'use strict';

var app = angular.module('sgManager');


app.factory('SessionService', function(){
  return {
    get: function(key)
    {
      return sessionStorage.getItem(key);
    },
    set: function(key, val) {
      return sessionStorage.setItem(key, val);
    },
    unset: function(key) {
      return sessionStorage.remove(key);
    }
  };
});

app.factory('AuthenticationService', function($http, $location, $base64, SessionService) {
  var cacheSession = function(credentials) {
    SessionService.set('credentials', credentials);
  };
  var uncacheSession = function() {
    SessionService.unset('credentials');
  };


  return {
    login: function(credentials) {
      $http.defaults.useXDomain = true;
      var login = $http({method: 'GET', url: 'http://localhost:8080/sg-manager-api/login', headers: {'Authorization': 'Basic ' + $base64.encode('admin' + ':' + 'admin')}});
      //var login = $http.post("localhost:8080/login", sanitizeCredentials(credentials));

      login.success(function() {
        cacheSession(credentials);}
       );
      return login;
    },
    logout: function() {
      uncacheSession();
    },
    isLoggedIn: function() {
      return SessionService.get('authenticated');
    }
  };
});