package dev.bhuwanupadhyay.demo.order.application.queries;

import dev.bhuwanupadhyay.demo.order.model.Order;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/orders")
public interface OrderQuery {

  static Map<String, Object> toOrderResponse(Order order) {
    Map<String, Object> response = new HashMap<>();
    response.put("orderId", order.getOrderId().S());
    response.put("customerId", order.getCustomerId().S());
    response.put("status", order.getStatus().name());
    return response;
  }

  static Map<String, Object> toLineItemResponse(LineItem lineItem) {
    Map<String, Object> response = new HashMap<>();
    response.put("orderId", lineItem.getOrderId().S());
    response.put("lineItemId", lineItem.getLineItemId().S());
    response.put("productId", lineItem.getProductId().S());
    response.put("quantity", lineItem.getQuantity());
    return response;
  }

  @GetMapping
  QueryListResponse getOrders(String filters, Pageable pageable);

  @GetMapping("/{orderId}/items")
  QueryListResponse getOrderLineItems(@PathVariable String orderId, String filters,
      Pageable pageable);

  @GetMapping("/{orderId}/items/{lineItemId}")
  QueryResponse getOrderLineItem(@PathVariable String orderId, @PathVariable String lineItemId);

  @GetMapping("/{orderId}")
  QueryResponse getOrder(@PathVariable String orderId);

  record QueryListResponse(List<Map<String, Object>> records, Long totalCount) {

  }

  record QueryResponse(Map<String, Object> record) {

  }

}
