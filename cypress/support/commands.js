// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
import 'cypress-keycloak';

Cypress.on('uncaught:exception', (err, origin) => {
  // Handle the error here
  // For example, you could log the error and continue with the test
  console.error(err);
  return false;
});

Cypress.Commands.overwrite('login', (originalFn) => {
  originalFn({
    root: 'http://localhost:9080',
    realm: 'semusings',
    username: 'user',
    password: 'user',
    client_id: 'web_app',
    redirect_uri: 'http://localhost:8080',
  })
})