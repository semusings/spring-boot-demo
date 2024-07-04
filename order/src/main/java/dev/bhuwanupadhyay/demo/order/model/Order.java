package dev.bhuwanupadhyay.demo.order.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

  @EmbeddedId
  private OrderId orderId;
  @Embedded
  private CustomerId customerId;
  @Enumerated(EnumType.STRING)
  private Status status;

  // Required for persistence layer
  protected Order() {
  }

  Order(CustomerId customerId) {
    this.orderId = new OrderId(UUID.randomUUID());
    this.customerId = customerId;
    this.status = Status.OPEN;
  }

  public void complete() {
    this.status = Status.COMPLETED;
  }

  public void cancel() {
    this.status = Status.CANCELLED;
  }

  public CustomerId getCustomerId() {
    return customerId;
  }

  public OrderId getOrderId() {
    return orderId;
  }

  public Status getStatus() {
    return status;
  }

  public enum Status {
    OPEN, COMPLETED, CANCELLED
  }

  @Entity
  @Table(name = "line_items")
  public static class LineItem implements Serializable {

    @EmbeddedId
    private LineItemId lineItemId;
    @Embedded
    private OrderId orderId;
    @Embedded
    private ProductId productId;
    private Integer quantity;

    // Required for persistence layer
    protected LineItem() {
    }

    LineItem(OrderId orderId) {
      this.lineItemId = new LineItemId(UUID.randomUUID());
      this.orderId = orderId;
    }

    public void updateProductQuantity(ProductId productId, Integer quantity) {
      if (quantity == null || quantity < 1) {
        throw new IllegalArgumentException(
            "At least 1 quantity should be selected for product: " + productId);
      }
      this.quantity = quantity;
      this.productId = productId;
    }

    public OrderId getOrderId() {
      return orderId;
    }

    public LineItemId getLineItemId() {
      return lineItemId;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public ProductId getProductId() {
      return productId;
    }

  }

  @Embeddable
  public record OrderId(UUID orderId) implements Serializable {

    public static OrderId of(String uuid) {
      return new OrderId(UUID.fromString(uuid));
    }

    public String S() {
      return orderId.toString();
    }

  }

  @Embeddable
  public record ProductId(UUID productId) implements Serializable {

    public static ProductId of(String uuid) {
      return new ProductId(UUID.fromString(uuid));
    }

    public String S() {
      return productId.toString();
    }
  }

  @Embeddable
  public record LineItemId(UUID lineItemId) implements Serializable {

    public static LineItemId of(String uuid) {
      return new LineItemId(UUID.fromString(uuid));
    }

    public String S() {
      return lineItemId.toString();
    }
  }

  @Embeddable
  public record CustomerId(UUID customerId) implements Serializable {

    public static CustomerId of(String uuid) {
      return new CustomerId(UUID.fromString(uuid));
    }

    public String S() {
      return customerId.toString();
    }
  }
}
