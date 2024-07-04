package dev.bhuwanupadhyay.demo.order.model;

public interface OrderPermission {

  void assign(Permission permission);

  record Permission(String orderId, String relation, String customerId) {

    public static Permission asOwner(Order order) {
      String orderId = order.getOrderId().S();
      String customerId = order.getCustomerId().S();
      return new Permission(orderId, "owner", customerId);
    }

  }
}

