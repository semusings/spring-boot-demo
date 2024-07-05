package dev.bhuwanupadhyay.demo.inventory.application.commands;

import dev.bhuwanupadhyay.demo.inventory.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/products")
public interface ProductCommand {

  @PostMapping()
  ProductResponse createProduct(@RequestBody ProductRequest request);

  @PostMapping("/{productId}/update-stock")
  ProductResponse updateStock(@PathVariable String productId,
      @RequestBody UpdateStockRequest request);

  record ProductRequest(String name, String description, Double retailPrice,
                        Integer initialQuantity) {

  }

  record UpdateStockRequest(Integer quantity) {

  }

  record ProductResponse(String productId, String name, String description, Double retailPrice,
                         String status) {

    public static ProductResponse of(Product product) {
      return new ProductResponse(product.getProductId().S(), product.getName(),
          product.getDescription(), product.getRetailPrice(), product.getStatus().name());
    }
  }
}
