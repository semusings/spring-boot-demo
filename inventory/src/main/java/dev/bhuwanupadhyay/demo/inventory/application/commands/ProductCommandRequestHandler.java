package dev.bhuwanupadhyay.demo.inventory.application.commands;


import dev.bhuwanupadhyay.demo.inventory.model.Product;
import dev.bhuwanupadhyay.demo.inventory.model.Product.ProductId;
import dev.bhuwanupadhyay.demo.inventory.model.ProductStockManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductCommandRequestHandler implements ProductCommand {

  private final ProductStockManager stockManager;

  public ProductCommandRequestHandler(ProductStockManager stockManager) {
    this.stockManager = stockManager;
  }

  @Override
  public ProductResponse createProduct(ProductRequest request) {
    Product product = stockManager.create(request.name(), request.description(),
        request.retailPrice(), request.initialQuantity());
    return ProductResponse.of(product);
  }

  @Override
  public ProductResponse updateStock(String productId, UpdateStockRequest request) {
    Product product = stockManager.update(ProductId.of(productId), request.quantity());
    return ProductResponse.of(product);
  }
}
