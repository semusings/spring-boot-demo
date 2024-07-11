package dev.bhuwanupadhyay.demo.payment.model;

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
@Table(name = "payments")
public class Payment implements Serializable {

    @EmbeddedId private PaymentId paymentId;
    @Embedded private OrderId orderId;
    @Embedded private CustomerId customerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double txnAmount;

    // Required for persistence layer
    protected Payment() {}

    Payment(OrderId orderId, CustomerId customerId) {
        this.paymentId = new PaymentId(UUID.randomUUID());
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = Status.INITIATED;
    }

    public void complete() {
        this.status = Status.COMPLETED;
    }

    public void updateAmount(Double txnAmount) {
        this.txnAmount = txnAmount;
    }

    public void refund() {
        this.status = Status.REFUNDED;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Double getTxnAmount() {
        return txnAmount;
    }

    public PaymentId getPaymentId() {
        return paymentId;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        INITIATED,
        COMPLETED,
        REFUNDED
    }

    @Embeddable
    public record PaymentId(UUID paymentId) implements Serializable {

        public static PaymentId of(String uuid) {
            return new PaymentId(UUID.fromString(uuid));
        }

        public String S() {
            return paymentId.toString();
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
    public record CustomerId(UUID customerId) implements Serializable {

        public static CustomerId of(String uuid) {
            return new CustomerId(UUID.fromString(uuid));
        }

        public String S() {
            return customerId.toString();
        }
    }
}
