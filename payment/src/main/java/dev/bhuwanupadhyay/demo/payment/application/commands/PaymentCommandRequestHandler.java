package dev.bhuwanupadhyay.demo.payment.application.commands;

import dev.bhuwanupadhyay.demo.payment.model.Payment;
import dev.bhuwanupadhyay.demo.payment.model.Payment.CustomerId;
import dev.bhuwanupadhyay.demo.payment.model.Payment.OrderId;
import dev.bhuwanupadhyay.demo.payment.model.PaymentProcessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PaymentCommandRequestHandler implements PaymentCommand {

  private final PaymentProcessor paymentProcessor;

  public PaymentCommandRequestHandler(PaymentProcessor paymentProcessor) {
    this.paymentProcessor = paymentProcessor;
  }

  @Override
  public PaymentResponse createPayment(String orderId, PaymentRequest request) {
    String customerId = SecurityContextHolder.getContext().getAuthentication().getName();
    Payment payment = paymentProcessor.create(OrderId.of(orderId), CustomerId.of(customerId),
        request.txnAmount());
    return PaymentResponse.of(payment);
  }

  @Override
  public PaymentResponse refundPayment(String orderId) {
    Payment payment = paymentProcessor.refund(OrderId.of(orderId));
    return PaymentResponse.of(payment);
  }
}
