module.exports = function (config) {
  config.set({
    frameworks: ['jasmine', '@angular/build'],
    browsers: ['ChromeHeadlessCI'],
    customLaunchers: {
      ChromeHeadlessCI: {
        base: 'ChromeHeadless',
        flags: ['--no-sandbox', '--disable-gpu', '--disable-dev-shm-usage'],
      },
    },
    reporters: ['progress'],
    singleRun: true,
    restartOnFileChange: false,
  });
};
