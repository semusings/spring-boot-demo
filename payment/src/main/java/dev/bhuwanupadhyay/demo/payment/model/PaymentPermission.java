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
    }
}
