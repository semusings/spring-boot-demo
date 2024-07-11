package dev.bhuwanupadhyay.demo.order.model;

import java.util.UUID;

public interface OrderPermission {

    void assign(Permission permission);

    record Permission(UUID orderId, String relation, UUID customerId) {

        public static Permission asOwner(Order order) {

            return new Permission(
                    order.getOrderId().orderId(), "owner", order.getCustomerId().customerId());
        }
    }
}
