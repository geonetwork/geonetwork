module.exports = {
  '/geonetwork': {
    target: 'http://localhost:7979',
    secure: true,
    logLevel: 'debug',
    changeOrigin: true,
  },
  '/api': {
    target: 'http://localhost:7979',
    secure: true,
    logLevel: 'debug',
    changeOrigin: true,
  },
};
