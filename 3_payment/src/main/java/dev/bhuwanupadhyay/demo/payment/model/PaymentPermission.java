package dev.bhuwanupadhyay.demo.payment.model;

import java.util.UUID;

public interface PaymentPermission {

    void assign(Permission permission);

    record Permission(UUID paymentId, UUID orderId, String relation, UUID customerId) {

        public static Permission asOwner(Payment payment) {
            UUID paymentId = payment.getPaymentId().paymentId();
            UUID orderId = payment.getOrderId().orderId();
            UUID customerId = payment.getCustomerId().customerId();
            return new Permission(paymentId, orderId, "owner", customerId);
        }

        public String userAsCustomer() {
            return String.format("customer:%s", customerId);
        }

        public String relationAsOwner() {
            return "owner";
        }

        public String objectAsPayment() {
            return String.format("payment:%s", paymentId);
        }

        public String objectAsOrder() {
            return String.format("order:%s", orderId);
        }
    }
}
