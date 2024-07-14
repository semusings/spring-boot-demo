import {ProductRequest, ProductResponse} from "@semusings/product"
import {fakerEN as en} from "@faker-js/faker";
// @ts-ignore
import {KcTokens} from "cypress-keycloak-commands"

describe("Product api testing", () => {

  beforeEach(() => {
    (cy as any).kcLogin("kcUser").as("tokens");
  });

  it("create product - POST", () => {
    cy.get<KcTokens>("@tokens").then((tokens: KcTokens) => {
      const requestBody: ProductRequest = {
        name: en.commerce.productName(),
        description: en.commerce.productDescription(),
        initialQuantity: Number(en.string.numeric({length: 2, allowLeadingZeros: true})),
        retailPrice: Number(en.commerce.price({min: 10, max: 50}))
      }
      cy.request({
        url: "/api/products", method: "POST", body: requestBody, auth: {
          bearer: tokens.access_token
        }
      }).then((response: any) => {
        expect(response.status).to.eq(200);
        const responseBody: ProductResponse = response.body;
        assert.isTrue(responseBody.name == requestBody.name, "Product name matched with request");
      });
    });
  });


  afterEach(() => {
    (cy as any).kcLogout();
  });
});