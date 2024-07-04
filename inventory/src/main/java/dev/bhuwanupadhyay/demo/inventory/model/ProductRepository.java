package dev.bhuwanupadhyay.demo.inventory.model;

import dev.bhuwanupadhyay.demo.inventory.model.Product.ProductId;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Product.class, idClass = ProductId.class)
interface ProductRepository {

  Product getByProductId(ProductId productId);

  Product save(Product product);

}
