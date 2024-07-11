package dev.bhuwanupadhyay.demo.order.model;

import dev.bhuwanupadhyay.demo.order.model.Order.LineItem;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItemId;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;

import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = LineItem.class, idClass = LineItemId.class)
interface OrderLineItemRepository {

    Optional<LineItem> getByOrderIdAndLineItemId(OrderId orderId, LineItemId lineItemId);

    LineItem save(LineItem lineItem);
}
