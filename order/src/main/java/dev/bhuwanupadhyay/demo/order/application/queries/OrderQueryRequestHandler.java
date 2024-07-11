package dev.bhuwanupadhyay.demo.order.application.queries;

import dev.bhuwanupadhyay.demo.order.model.Order;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItem;
import dev.bhuwanupadhyay.demo.order.model.Order.LineItemId;
import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class OrderQueryRequestHandler implements OrderQuery {

    private final OrderJpaRepository orders;
    private final OrderLineItemJpaRepository lineItems;

    public OrderQueryRequestHandler(
            OrderJpaRepository orders, OrderLineItemJpaRepository lineItems) {
        this.orders = orders;
        this.lineItems = lineItems;
    }

    @Override
    public QueryListResponse getOrders(String filters, Pageable pageable) {
        Page<Order> page = orders.findAll(OrderJpaRepository.filters(filters), pageable);
        List<Map<String, Object>> records =
                page.stream().map(OrderQuery::toOrderResponse).collect(Collectors.toList());
        return new QueryListResponse(records, page.getTotalElements());
    }

    @Override
    public QueryListResponse getOrderLineItems(String orderId, String filters, Pageable pageable) {
        Page<LineItem> page =
                lineItems.findAll(
                        OrderLineItemJpaRepository.filters(OrderId.of(orderId), filters), pageable);
        List<Map<String, Object>> records =
                page.stream().map(OrderQuery::toLineItemResponse).collect(Collectors.toList());
        return new QueryListResponse(records, page.getTotalElements());
    }

    @Override
    public QueryResponse getOrderLineItem(String orderId, String lineItemId) {
        LineItem lineItem =
                lineItems
                        .getByOrderIdAndLineItemId(OrderId.of(orderId), LineItemId.of(lineItemId))
                        .orElseThrow(() -> new IllegalArgumentException("Not Found"));
        return new QueryResponse(OrderQuery.toLineItemResponse(lineItem));
    }

    @Override
    public QueryResponse getOrder(String orderId) {
        Order order =
                orders.getByOrderId(OrderId.of(orderId))
                        .orElseThrow(() -> new IllegalArgumentException("Not Found"));
        return new QueryResponse(OrderQuery.toOrderResponse(order));
    }
}
