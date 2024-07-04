package dev.bhuwanupadhyay.demo.order.model;

import dev.bhuwanupadhyay.demo.order.model.Order.LineItem;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItemId;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;
import java.util.Optional;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = LineItem.class, idClass = LineItemId.class)
interface OrderLineItemRepository {

  Optional<LineItem> getByOrderIdAndLineItemId(OrderId orderId, LineItemId lineItemId);

  LineItem save(LineItem lineItem);

}
