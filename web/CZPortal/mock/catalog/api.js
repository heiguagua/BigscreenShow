const Router = require('express').Router(),
      Util = require('../common/util.js');
/** Router definition */
Router.route('/combing/deptInfo')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/deptInfo.json'));
});
Router.route('/combing/dirNum')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/dirNum.json'));
});
Router.route('/combing/itemNum')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/itemNum.json'));
});
Router.route('/combing/systemNum')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/systemNum.json'));
});
Router.route('/depDataInfo/dataSetNum')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/dataSetNum.json'));
});
Router.route('/depDataInfo/themeInfo')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/themeInfo.json'));
});
Router.route('/depDataInfo/subjectInfo')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/subjectInfo.json'));
});
Router.route('/depDataInfo/invokeNumByApp')
  .get(function(request, response) {
    response.json(Util.json('/catalog/json/invokeNumByApp.json'));
});

/** Module export */
module.exports = Router;
