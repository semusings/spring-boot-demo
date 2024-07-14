const {defineConfig} = require("cypress");

module.exports = defineConfig({
  env: {
    auth_base_url: "http://localhost:9080",
    auth_realm: "semusings",
    auth_client_id: "web_app"
  }, e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    }, baseUrl: 'http://localhost:8080',
  },
});
