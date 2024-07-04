package dev.bhuwanupadhyay.demo.payment.model;

public interface PaymentPermission {

  void assign(Permission permission);

  record Permission(String paymentId, String orderId, String relation, String customerId) {

    public static Permission asOwner(Payment payment) {
      String paymentId = payment.getPaymentId().S();
      String orderId = payment.getOrderId().S();
      String customerId = payment.getCustomerId().S();
      return new Permission(paymentId, orderId, "owner", customerId);
    }

  }
}

