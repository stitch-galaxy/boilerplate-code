'use strict';

var app = angular.module('sgManager');

app.factory('ProductsManagerService', function($http, $base64, SessionService) {

  return {
    getProducts: function(pageNumber, itemsPerPage) {

      var data = $http({method: 'GET', url: 'https://localhost/api/getProducts', headers: {'Authorization': 'Basic ' + $base64.encode('admin' + ':' + 'admin')}});

      data.success(function() {
      });
      return data;
    }
  };
});