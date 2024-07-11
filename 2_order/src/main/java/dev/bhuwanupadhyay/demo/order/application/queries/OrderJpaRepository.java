package dev.bhuwanupadhyay.demo.order.application.queries;

import dev.bhuwanupadhyay.demo.order.model.Order;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = Order.class, idClass = OrderId.class)
interface OrderJpaRepository {

    static Specification<Order> filters(String filters) {
        return (root, query, cb) -> {
            //
            return cb.and(cb.equal(root.get("status"), Order.Status.OPEN));
        };
    }

    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

    Optional<Order> getByOrderId(OrderId orderId);
}
