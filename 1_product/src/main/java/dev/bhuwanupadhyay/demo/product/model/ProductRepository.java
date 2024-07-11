package dev.bhuwanupadhyay.demo.product.model;

import dev.bhuwanupadhyay.demo.product.model.Product.ProductId;

import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Product.class, idClass = ProductId.class)
interface ProductRepository {

    Product getByProductId(ProductId productId);

    Product save(Product product);
}
