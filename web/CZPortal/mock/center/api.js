const Router = require('express').Router(),
      Util = require('../common/util.js');
/** Router definition */
Router.route('/sixApp/vehicle')
  .get(function(request, response) {
    response.json(Util.json('/center/json/vehicle.json'));
});
  Router.route('/sixApp/vehicleView')
  .get(function(request, response) {
    response.json(Util.json('/center/json/vehicleView.json'));
});
    Router.route('/sixApp/civilServant')
  .get(function(request, response) {
    response.json(Util.json('/center/json/civilServant.json'));
});
  Router.route('/sixApp/governmentApprovalSystem')
  .get(function(request, response) {
    response.json(Util.json('/center/json/governmentApprovalSystem.json'));
});
    Router.route('/sixApp/meteorologicalObservationStation')
  .get(function(request, response) {
    response.json(Util.json('/center/json/meteorologicalObservationStation.json'));
});
    Router.route('/sixApp/mos_maxTemperature')
  .get(function(request, response) {
    response.json(Util.json('/center/json/mos_maxTemperature.json'));
});
Router.route('/sixApp/mos_minTemperature')
  .get(function(request, response) {
    response.json(Util.json('/center/json/mos_minTemperature.json'));
});
  Router.route('/sixApp/mos_maxRainfall')
  .get(function(request, response) {
    response.json(Util.json('/center/json/mos_maxRainfall.json'));
});
    Router.route('/sixApp/weatherForecast')
  .get(function(request, response) {
    response.json(Util.json('/center/json/weatherForecast.json'));
});
      Router.route('/sixApp/creditWebOne')
  .get(function(request, response) {
    response.json(Util.json('/center/json/creditWebOne.json'));
});
        Router.route('/sixApp/disasterWarning')
  .get(function(request, response) {
    response.json(Util.json('/center/json/disasterWarning.json'));
});
          Router.route('/sixApp/creditWebTwo')
  .get(function(request, response) {
    response.json(Util.json('/center/json/creditWebTwo.json'));
});
            Router.route('/sixApp/creditWebThree')
  .get(function(request, response) {
    response.json(Util.json('/center/json/creditWebThree.json'));
});

/** Module export */
module.exports = Router;
