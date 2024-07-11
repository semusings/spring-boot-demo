package dev.bhuwanupadhyay.demo.inventory.application.queries;

import dev.bhuwanupadhyay.demo.inventory.model.Product;
import dev.bhuwanupadhyay.demo.inventory.model.Product.ProductId;
import dev.bhuwanupadhyay.demo.inventory.model.Product.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = Product.class, idClass = ProductId.class)
interface ProductJpaRepository {

    static Specification<Product> filters(String filters) {
        return (root, query, cb) -> {
            //
            return cb.and(cb.equal(root.get("status"), Status.AVAILABLE));
        };
    }

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    Optional<Product> getByProductId(ProductId orderId);
}
