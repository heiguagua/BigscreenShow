const Router = require('express').Router(),
      Util = require('../common/util.js');
/** Router definition */
Router.route('/serverMonitor/detail')
  .get(function(request, response) {
    response.json(Util.json('/serverMonitor/json/detail.json'));
});
Router.route('/serverMonitor/detailTime')
  .get(function(request, response) {
    response.json(Util.json('/serverMonitor/json/detailTime.json'));
});
/** Module export */
module.exports = Router;
