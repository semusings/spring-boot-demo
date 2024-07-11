package dev.bhuwanupadhyay.demo.product.model;

import java.util.UUID;

public interface ProductPermission {

    void assign(Permission permission);

    record Permission(UUID productId) {

        public static Permission asOwner(Product product) {
            UUID productId = product.getProductId().productId();
            return new Permission(productId);
        }

        public String userAsAny() {
            return String.format("user:%s", "*");
        }

        public String objectAsProduct() {
            return String.format("product:%s", productId);
        }

        public String relationAsManager() {
            return "manager";
        }
    }
}
