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

      /** HTTP Interceptor */
      $httpProvider.interceptors.push(interceptor);
      interceptor.$inject = ['$q', '$location'];
      function interceptor($q, $location) {
        return {
          'request': function(config) {
            var screen_width = screen.width;
            var screen_height = screen.height;
            var path = $location.path();
            //$('body').css({'font-size':document.documentElement.clientWidth / 6.4 + 'px'});
            setTimeout(function(){
              if(path.indexOf('main') > -1) {
                $('.section').css({'height':screen_height/2 +'px','width':screen_width/2+'px'});
                $('.section-main').css({'height':screen_height +'px','width':screen_width+'px'});
                $('.section-center').css({'height':screen_height/2 +'px','width':screen_width+'px'});
                $('.section h1').css({'line-height':'2rem'});
                $('.chart-idc-use').css({'height':'66%'});
                $('.idcuse-desc p').css({'font-size':'0.9rem'});
                $('.idcuse-desc p strong').css({'font-size':'1rem'});
              }
              if(path.indexOf('dept') > -1 || path.indexOf('standard') > -1 || path.indexOf('support') > -1 || path.indexOf('datamap') > -1  || path.indexOf('idcuse') > -1  || path.indexOf('bigdata') > -1 ) {
                $('.section').css({'height':screen_height+'px','width':screen_width+'px'});
              }
              if(path.indexOf('dashboard') > -1) {
                $('html').css({'font-size':'16px'});
                $('#dashboard').css({'height':screen_height +'px','width':screen_width+'px'});
                $('.section').css({'height':screen_height/2 +'px','width':screen_width/4+'px'});
                $('.section-center').css({'height':screen_height/2 +'px','width':screen_width/2+'px'});
                $('.section.support').css({'position':'absolute','top':screen_height/2 +'px','left':0});
                $('.section.city-datamap').css({'position':'absolute','top':screen_height/2 +'px','left':screen_width/4+'px'});
                $('.section.idcuse').css({'position':'absolute','top':screen_height/2 +'px','right':screen_width/4+'px'});
                $('.section.bigdata').css({'position':'absolute','top':screen_height/2 +'px','right':0});
              }
            },500)
            return config;

          },
          'response': function(response) {
            $q.when(response, function(result){

            });
            return response;
          }
        };
      };
  };

})();
