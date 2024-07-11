package dev.bhuwanupadhyay.demo.order.application.commands;

import dev.bhuwanupadhyay.demo.order.model.Order;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/orders")
public interface OrderCommand {

    @PostMapping
    OrderResponse createOrder();

    @PostMapping("/{orderId}/items")
    OrderResponse addItem(@PathVariable String orderId, ItemRequest request);

    @PostMapping("/{orderId}/items/{lineItemId}")
    OrderResponse updateItem(
            @PathVariable String orderId,
            @PathVariable String lineItemId,
            @RequestBody ItemRequest request);

    @PostMapping("/{orderId}/complete")
    OrderResponse completeOrder(@PathVariable String orderId);

    @PostMapping("/{orderId}/cancel")
    OrderResponse cancelOrder(@PathVariable String orderId);

    record ItemRequest(String productId, Integer quantity) {}

    record OrderResponse(String orderId, String status, String customerId) {

        public static OrderResponse of(Order order) {
            return new OrderResponse(
                    order.getOrderId().S(), order.getStatus().name(), order.getCustomerId().S());
        }
    }
}
