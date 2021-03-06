const Express = require('express'),
      App = Express(),
      Cors = require('cors'),
      BodyParser = require('body-parser'),
      Middleware = require('./common/middleware.js'),
      Color = require('colors/safe');

/** Middlewares */
App.use('/dev', Express.static('./artifact'));
App.use('/pro', Express.static('./release'));
App.use(Cors({
  origin: 'http://localhost:6009',
  methods: 'GET, POST, PUT, DELETE, OPTIONS',
  allowedHeaders: ['Content-Type', 'Authorization'],
  credentials: true,
  maxAge: 1728000
}));
App.use(BodyParser.json());
App.use('/', (request, response, next) => {
  Middleware.log(request, response);
  next();
});
App.listen(6008);

/* Informations */
console.info(Color.blue('Livereload  started on http://localhost:6009'));
console.info(Color.blue('Development started on http://localhost:6008/dev'));
console.info(Color.blue('Production  started on http://localhost:6008/pro'));

/** Routers */
App.use('/', require('./deptdata/api'));
App.use('/', require('./serverMonitor/api'));
App.use('/', require('./status/api'));
App.use('/', require('./center/api'));
App.use('/', require('./catalog/api'));
// App.use('/', require('./repository/api'));
