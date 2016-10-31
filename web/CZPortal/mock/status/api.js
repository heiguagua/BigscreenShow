const Router = require('express').Router(),
      Util = require('../common/util.js');
/** Router definition */
Router.route('/status/get')
  .get(function(request, response) {
    response.json(Util.json('/status/json/get.json'));
});
Router.route('/status/set')
  .put(function(request, response) {
    response.json(Util.json('/status/json/get.json'));
});
/** Module export */
module.exports = Router;
