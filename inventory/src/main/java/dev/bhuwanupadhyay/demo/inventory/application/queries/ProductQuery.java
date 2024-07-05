package dev.bhuwanupadhyay.demo.inventory.application.queries;

import dev.bhuwanupadhyay.demo.inventory.model.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/products")
public interface ProductQuery {

  static Map<String, Object> toProductResponse(Product product) {
    Map<String, Object> response = new HashMap<>();
    response.put("productId", product.getProductId().S());
    response.put("name", product.getName());
    response.put("description", product.getDescription());
    response.put("retailPrice", product.getRetailPrice());
    response.put("availableQuantity", product.getAvailableQuantity());
    response.put("status", product.getStatus().name());
    return response;
  }

  @GetMapping
  QueryListResponse getProducts(String filters, Pageable pageable);

  @GetMapping("/{productId}")
  QueryResponse getProduct(@PathVariable String productId);

  record QueryListResponse(List<Map<String, Object>> records, Long totalCount) {

  }

  record QueryResponse(Map<String, Object> record) {

  }

}
