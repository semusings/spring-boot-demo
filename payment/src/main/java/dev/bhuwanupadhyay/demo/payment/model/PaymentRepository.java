package dev.bhuwanupadhyay.demo.payment.model;

import dev.bhuwanupadhyay.demo.payment.model.Payment.OrderId;
import dev.bhuwanupadhyay.demo.payment.model.Payment.PaymentId;

import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Payment.class, idClass = PaymentId.class)
interface PaymentRepository {

    Payment getByPaymentId(PaymentId paymentId);

    Payment getByOrderId(OrderId orderId);

    Payment save(Payment payment);
}
