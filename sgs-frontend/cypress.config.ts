import { defineConfig } from 'cypress';
// eslint-disable-next-line @typescript-eslint/no-require-imports
const registerPlugin = require('cypress-testrail-simple/src/plugin') as (on: Cypress.PluginEvents, config: Cypress.PluginConfigOptions) => void;

// Integración con TestRail vía cypress-testrail-simple (plugin, no reporter).
// Posproc resultados al final de cada `cypress run`, sin bloquear el arranque.
// Variables env requeridas en CI:
//   TESTRAIL_HOST     (ej: https://sgsmarket.testrail.io)
//   TESTRAIL_USERNAME (email)
//   TESTRAIL_PASSWORD (API key)
//   TESTRAIL_PROJECT_ID
//   TESTRAIL_SUITE_ID
// Si faltan, el plugin se desactiva silenciosamente y los tests corren normales.

export default defineConfig({
  e2e: {
    baseUrl: 'https://sgsmarket.duckdns.org',
    specPattern: 'cypress/e2e/**/*.cy.ts',
    supportFile: 'cypress/support/e2e.ts',
    viewportWidth: 1280,
    viewportHeight: 720,
    defaultCommandTimeout: 10000,
    video: false,
    screenshotOnRunFailure: true,
    reporter: 'spec',

    setupNodeEvents(on, config) {
      registerPlugin(on, config);
      return config;
    },

    env: {
      testUserEmail: process.env['TEST_USER_EMAIL'] ?? 'testuser@sgs.com',
      testUserPass: process.env['TEST_USER_PASS'] ?? 'TestPass123!',
      adminEmail: process.env['ADMIN_EMAIL'] ?? 'admin@sgs.com',
      adminPass: process.env['ADMIN_PASSWORD'] ?? '',
    },
  },
});
