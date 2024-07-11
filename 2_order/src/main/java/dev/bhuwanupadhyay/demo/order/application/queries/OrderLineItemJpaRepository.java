package dev.bhuwanupadhyay.demo.order.application.queries;

import static dev.bhuwanupadhyay.demo.order.model.Order.LineItem;
import static dev.bhuwanupadhyay.demo.order.model.Order.LineItemId;

import dev.bhuwanupadhyay.demo.order.model.Order;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = LineItem.class, idClass = LineItemId.class)
interface OrderLineItemJpaRepository {

    static Specification<Order> filters(OrderId orderId, String filters) {
        return (root, query, cb) -> {
            //
            return cb.and(cb.equal(root.get("orderId"), orderId));
        };
    }

    Page<LineItem> findAll(Specification<Order> spec, Pageable pageable);

    Optional<LineItem> getByOrderIdAndLineItemId(OrderId orderId, LineItemId lineItemId);
}
