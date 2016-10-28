'use strict';

(function() {

  angular.module('app', [
    'ngAnimate',
    'ui.router',
    'common.http',
    'app.dashboard'
  ])
  .config(config);

  config.$inject = ['$stateProvider', '$urlRouterProvider', '$httpProvider'];

  function config($stateProvider, $urlRouterProvider, $httpProvider) {
    /** UI-Router Config */
    $urlRouterProvider.otherwise('/dashboard');
    $stateProvider
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: 'partials/dashboard/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('standard', {
        url: '/standard',
        templateUrl: 'partials/standard/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('center', {
        url: '/center',
        templateUrl: 'partials/center/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('main', {
        url: '/main',
        templateUrl: 'partials/main/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('bigdata', {
        url: '/bigdata',
        templateUrl: 'partials/bigdata/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('support', {
        url: '/support',
        templateUrl: 'partials/support/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('datamap', {
        url: '/datamap',
        templateUrl: 'partials/datamap/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('idcuse', {
        url: '/idcuse',
        templateUrl: 'partials/idcuse/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      })
      .state('dept', {
        url: '/dept',
        templateUrl: 'partials/dept/view.html',
        //templateUrl: 'circle/qzc.html',
        controller: 'dashboardController',
        controllerAs: 'dashboard',
      });
  };

})();
