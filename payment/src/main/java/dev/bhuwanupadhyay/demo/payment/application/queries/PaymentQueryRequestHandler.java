package dev.bhuwanupadhyay.demo.payment.application.queries;

import dev.bhuwanupadhyay.demo.payment.model.Payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class PaymentQueryRequestHandler implements PaymentQuery {

    private final PaymentJpaRepository orders;

    public PaymentQueryRequestHandler(PaymentJpaRepository orders) {
        this.orders = orders;
    }

    @Override
    public QueryListResponse getPayments(String filters, Pageable pageable) {
        Page<Payment> page =
                orders.findAll(
                        PaymentJpaRepository.filters(PaymentQuery.customerId(), filters), pageable);
        List<Map<String, Object>> records =
                page.stream().map(PaymentQuery::toPaymentResponse).collect(Collectors.toList());
        return new QueryListResponse(records, page.getTotalElements());
    }

    @Override
    public QueryResponse getPayment(String orderId) {
        Payment payment =
                orders.getByCustomerIdAndOrderId(
                                PaymentQuery.customerId(), Payment.OrderId.of(orderId))
                        .orElseThrow(() -> new IllegalArgumentException("Not Found"));
        return new QueryResponse(PaymentQuery.toPaymentResponse(payment));
    }
}
