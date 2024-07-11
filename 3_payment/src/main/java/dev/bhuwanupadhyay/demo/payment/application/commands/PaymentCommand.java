package dev.bhuwanupadhyay.demo.payment.application.commands;

import dev.bhuwanupadhyay.demo.payment.model.Payment;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/payments")
public interface PaymentCommand {

    @PostMapping("/{orderId}")
    PaymentResponse createPayment(
            @PathVariable String orderId, @RequestBody PaymentRequest request);

    @PostMapping("/{orderId}/refund")
    PaymentResponse refundPayment(@PathVariable String orderId);

    record PaymentRequest(Double txnAmount) {}

    record PaymentResponse(String paymentId, String orderId, String status, String customerId) {

        public static PaymentResponse of(Payment order) {
            return new PaymentResponse(
                    order.getPaymentId().S(),
                    order.getOrderId().S(),
                    order.getStatus().name(),
                    order.getCustomerId().S());
        }
    }
}
