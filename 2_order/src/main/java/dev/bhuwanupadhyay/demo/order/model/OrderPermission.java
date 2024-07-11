package dev.bhuwanupadhyay.demo.order.model;

import java.util.UUID;

public interface OrderPermission {

    void assign(Permission permission);

    record Permission(UUID orderId, UUID customerId) {

        public static Permission asOwner(Order order) {
            return new Permission(order.getOrderId().orderId(), order.getCustomerId().customerId());
        }

        public String userAsCustomer() {
            return String.format("customer:%s", customerId);
        }

        public String relationAsOwner() {
            return "owner";
        }

        public String objectAsOrder() {
            return String.format("order:%s", orderId);
        }
    }
}
