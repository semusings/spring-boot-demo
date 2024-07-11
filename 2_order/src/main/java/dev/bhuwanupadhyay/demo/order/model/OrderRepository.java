package dev.bhuwanupadhyay.demo.order.model;

import dev.bhuwanupadhyay.demo.order.model.Order.OrderId;

import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Order.class, idClass = OrderId.class)
interface OrderRepository {

    Order getByOrderId(OrderId orderId);

    Order save(Order order);
}
