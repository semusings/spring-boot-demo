package dev.bhuwanupadhyay.demo.inventory.model;

import java.util.UUID;

public interface ProductPermission {

  void assign(Permission permission);

  record Permission(UUID productId, String relation, AnyId customerId) {

    public static Permission asOwner(Product product) {
      UUID productId = product.getProductId().productId();
      return new Permission(productId, "owner", new AnyId());
    }

  }

  record AnyId() {

    @Override
    public String toString() {
      return "";
    }
  }
}

