package dev.bhuwanupadhyay.demo.payment.model;

import dev.bhuwanupadhyay.demo.payment.model.Payment.CustomerId;
import dev.bhuwanupadhyay.demo.payment.model.Payment.OrderId;
import dev.bhuwanupadhyay.demo.payment.model.PaymentPermission.Permission;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentProcessor {

    private final PaymentRepository payments;
    private final PaymentPermission permission;

    PaymentProcessor(PaymentRepository payments, PaymentPermission permission) {
        this.payments = payments;
        this.permission = permission;
    }

    @PreAuthorize("#customerId != null")
    public Payment create(
            OrderId orderId, @P("customerId") CustomerId customerId, Double txnAmount) {
        Payment payment = this.payments.save(new Payment(orderId, customerId));
        payment.updateAmount(txnAmount);
        payment.complete();
        this.permission.assign(Permission.asOwner(payment));
        return payment;
    }

    @PreAuthorize("@fga.check('payment', #orderId, 'refund', 'user')")
    public Payment refund(@P("orderId") OrderId orderId) {
        Payment order = this.payments.getByOrderId(orderId);
        order.refund();
        return this.payments.save(order);
    }
}
