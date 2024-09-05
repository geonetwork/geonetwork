module.exports = {
  '/geonetwork': {
    target: 'http://localhost:8080',
    secure: true,
    logLevel: 'debug',
    changeOrigin: true,
    cookiePathRewrite: {
      '/geonetwork': '/',
    },
  },
};
