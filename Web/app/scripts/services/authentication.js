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

app.factory('AuthenticationService', function($http, $location, SessionService) {
  var cacheSession = function() {
    SessionService.set('authenticated', true);
  };
  var uncacheSession = function() {
    SessionService.unset('authenticated');
  };

  var doNothing = function() {
  };


  return {
    login: function(credentials) {
      cacheSession();
      $location.path('/home');
    },
    logout: function() {
      uncacheSession();
    },
    isLoggedIn: function() {
      return SessionService.get('authenticated');
    }
  };
});