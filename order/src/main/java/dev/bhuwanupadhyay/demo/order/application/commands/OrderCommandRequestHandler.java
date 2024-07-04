package dev.bhuwanupadhyay.demo.order.application.commands;

import dev.bhuwanupadhyay.demo.order.model.Order;
import dev.bhuwanupadhyay.demo.order.model.Order.CustomerId;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItemId;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;
import dev.bhuwanupadhyay.demo.order.model.Order.ProductId;
import dev.bhuwanupadhyay.demo.order.model.OrderManagement;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OrderCommandRequestHandler implements OrderCommand {

  private final OrderManagement orderManagement;

  public OrderCommandRequestHandler(OrderManagement orderManagement) {
    this.orderManagement = orderManagement;
  }

  @Override
  public OrderResponse createOrder() {
    String customerId = SecurityContextHolder.getContext().getAuthentication().getName();
    Order order = orderManagement.create(CustomerId.of(customerId));
    return OrderResponse.of(order);
  }

  @Override
  public OrderResponse addItem(String orderId, ItemRequest request) {
    Order order = orderManagement.addItem(OrderId.of(orderId), ProductId.of(request.productId()),
        request.quantity());
    return OrderResponse.of(order);
  }

  @Override
  public OrderResponse updateItem(String orderId, String lineItemId, ItemRequest request) {
    Order order = orderManagement.updateItem(OrderId.of(orderId), LineItemId.of(lineItemId),
        ProductId.of(request.productId()), request.quantity());
    return OrderResponse.of(order);
  }

  @Override
  public OrderResponse completeOrder(String orderId) {
    Order order = orderManagement.complete(OrderId.of(orderId));
    return OrderResponse.of(order);
  }

  @Override
  public OrderResponse cancelOrder(String orderId) {
    Order order = orderManagement.cancel(OrderId.of(orderId));
    return OrderResponse.of(order);
  }
}
