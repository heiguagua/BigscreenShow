const Router = require('express').Router(),
      Util = require('../common/util.js');
/** Router definition */
Router.route('/depDataInfo/quantity')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/quantity.json'));
});
Router.route('/depDataInfo/table')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/table.json'));
});
Router.route('/depDataInfo/column')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/column.json'));
});
Router.route('/depDataInfo/dataCountLast')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/dataCountLast.json'));
});
Router.route('/depDataInfo/dataCount')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/dataCount.json'));
});
  Router.route('/depDataInfo/accessDataNum')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/accessDataNum.json'));
});
  Router.route('/combing/ResourceCombing')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/ResourceCombing.json'));
});
    Router.route('/depDataInfo/industrialPolicy')
  .get(function(request, response) {
    response.json(Util.json('/deptdata/json/industrialPolicy.json'));
});
  
/** Module export */
module.exports = Router;
