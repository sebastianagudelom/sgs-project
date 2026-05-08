import { defineConfig } from 'cypress';

// NOTA: el reporter de TestRail está deshabilitado por defecto.
// Para activarlo (cuando tengas TestRail configurado):
//   1. Quita la línea `reporter: 'spec'` de abajo
//   2. Descomenta el bloque `// reporter: 'cypress-testrail-reporter', ...`
//   3. Setea las env vars TESTRAIL_HOST/USER/API_KEY/PROJECT_ID/SUITE_ID

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
    reporter: 'cypress-testrail-reporter',
    reporterOptions: {
      host: process.env['TESTRAIL_HOST'] ?? '',
      username: process.env['TESTRAIL_USER'] ?? '',
      password: process.env['TESTRAIL_API_KEY'] ?? '',
      projectId: process.env['TESTRAIL_PROJECT_ID'] ?? '',
      suiteId: process.env['TESTRAIL_SUITE_ID'] ?? '',
      includeAllInTestRun: false,
    },

    env: {
      testUserEmail: process.env['TEST_USER_EMAIL'] ?? 'testuser@sgs.com',
      testUserPass: process.env['TEST_USER_PASS'] ?? 'TestPass123!',
      adminEmail: process.env['ADMIN_EMAIL'] ?? 'admin@sgs.com',
      adminPass: process.env['ADMIN_PASSWORD'] ?? '',
    },
  },
});
