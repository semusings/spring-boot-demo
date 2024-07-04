package dev.bhuwanupadhyay.demo.inventory.model;

public interface ProductPermission {

  void assign(Permission permission);

  record Permission(String product, String relation, String customerId) {

    public static Permission asOwner(Product product) {
      String productId = product.getProductId().S();
      return new Permission(productId, "owner", "*");
    }

  }
}

