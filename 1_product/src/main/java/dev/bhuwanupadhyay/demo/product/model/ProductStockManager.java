package dev.bhuwanupadhyay.demo.product.model;

import dev.bhuwanupadhyay.demo.product.model.Product.ProductId;
import dev.bhuwanupadhyay.demo.product.model.ProductPermission.Permission;

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

    @PreAuthorize("@fga.check('product', '*', 'manager', 'user')")
    public Product create(
            String name, String description, Double retailPrice, Integer initialQuantity) {
        Product product =
                this.products.save(new Product(name, description, retailPrice, initialQuantity));
        this.permission.assign(Permission.asOwner(product));
        return product;
    }

    @PreAuthorize("@fga.check('product', #productId, 'manager', 'user')")
    public Product update(@P("productId") ProductId productId, Integer quantity) {
        Product product = this.products.getByProductId(productId);
        product.updateStock(quantity);
        return this.products.save(product);
    }
}
