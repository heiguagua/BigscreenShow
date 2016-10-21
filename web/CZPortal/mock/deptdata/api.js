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
/** Module export */
module.exports = Router;