import {ProductRequest, ProductResponse} from "@semusings/product"
import {fakerEN as en} from "@faker-js/faker";

describe("Product api testing", () => {

  beforeEach(() => {
    (cy as any).login();
  });

  it("create product - POST", () => {
    const request: ProductRequest = {
      name: en.commerce.productName(),
      description: en.commerce.productDescription(),
      initialQuantity: Number(en.string.numeric(100)),
      retailPrice: Number(en.commerce.price({min: 10, max: 50}))
    }

    cy.request("/api/products", request).as("createProduct");

    cy.get("@createProduct").then((rs: any) => {
      expect(rs.status).to.eq(200);
      const data: ProductResponse = rs.body.data;
      assert.isTrue(data.name == request.name, "Product name matched with request");
    });
  });


  afterEach(() => {
    (cy as any).logout();
  });
});