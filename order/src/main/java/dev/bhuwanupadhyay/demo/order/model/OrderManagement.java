package dev.bhuwanupadhyay.demo.order.model;

import static dev.bhuwanupadhyay.demo.order.model.Order.ProductId;

import dev.bhuwanupadhyay.demo.order.model.Order.CustomerId;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItem;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItemId;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;
import dev.bhuwanupadhyay.demo.order.model.OrderPermission.Permission;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderManagement {

  private final OrderRepository orders;
  private final OrderLineItemRepository orderLineItems;
  private final OrderPermission permission;

  OrderManagement(OrderRepository orders, OrderLineItemRepository orderLineItems,
      OrderPermission permission) {
    this.orders = orders;
    this.orderLineItems = orderLineItems;
    this.permission = permission;
  }

  @PreAuthorize("#customerId != null")
  public Order create(@P("customerId") CustomerId customerId) {
    Order order = this.orders.save(new Order(customerId));
    this.permission.assign(Permission.asOwner(order));
    return order;
  }

  @PreAuthorize("@fga.check('order', #orderId, 'owner', 'customer')")
  public Order addItem(@P("orderId") OrderId orderId, ProductId productId, Integer quantity) {
    Order order = this.orders.getByOrderId(orderId);
    LineItem lineItem = new LineItem(orderId);
    lineItem.updateProductQuantity(productId, quantity);
    LineItem ignored = this.orderLineItems.save(lineItem);
    return this.orders.save(order);
  }

  @PreAuthorize("@fga.check('order', #orderId, 'owner', 'customer')")
  public Order updateItem(@P("orderId") OrderId orderId, LineItemId lineItemId, ProductId productId,
      Integer quantity) {
    Order order = this.orders.getByOrderId(orderId);
    LineItem lineItem = this.orderLineItems.getByOrderIdAndLineItemId(orderId, lineItemId)
        .orElseThrow(() -> new IllegalArgumentException("Not Found"));
    lineItem.updateProductQuantity(productId, quantity);
    return this.orders.save(order);
  }

  @PreAuthorize("@fga.check('order', #orderId, 'owner', 'customer')")
  public Order complete(@P("orderId") OrderId orderId) {
    Order order = this.orders.getByOrderId(orderId);
    order.complete();
    return this.orders.save(order);
  }

  @PreAuthorize("@fga.check('order', #orderId, 'owner', 'user')")
  public Order cancel(@P("orderId") OrderId orderId) {
    Order order = this.orders.getByOrderId(orderId);
    order.cancel();
    return this.orders.save(order);
  }
}
