package dev.bhuwanupadhyay.demo.inventory.model;

import dev.bhuwanupadhyay.demo.inventory.model.Product.ProductId;
import dev.bhuwanupadhyay.demo.inventory.model.ProductPermission.Permission;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductStockManager {

    private final ProductRepository products;
    private final ProductPermission permission;

    ProductStockManager(ProductRepository products, ProductPermission permission) {
        this.products = products;
        this.permission = permission;
    }

    public Product create(
            String name, String description, Double retailPrice, Integer initialQuantity) {
        Product product =
                this.products.save(new Product(name, description, retailPrice, initialQuantity));
        this.permission.assign(Permission.asOwner(product));
        return product;
    }

    @PreAuthorize("@fga.check('inventory', #productId, 'update', 'user')")
    public Product update(@P("productId") ProductId productId, Integer quantity) {
        Product product = this.products.getByProductId(productId);
        product.updateStock(quantity);
        return this.products.save(product);
    }
}
