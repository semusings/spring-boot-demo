package dev.bhuwanupadhyay.demo.payment.application.queries;

import dev.bhuwanupadhyay.demo.payment.model.Payment;
import dev.bhuwanupadhyay.demo.payment.model.Payment.CustomerId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/payments")
public interface PaymentQuery {

  static Map<String, Object> toPaymentResponse(Payment payment) {
    Map<String, Object> response = new HashMap<>();
    response.put("paymentId", payment.getPaymentId().S());
    response.put("orderId", payment.getOrderId().S());
    response.put("customerId", payment.getCustomerId().S());
    response.put("txnAmount", payment.getTxnAmount());
    response.put("status", payment.getStatus().name());
    return response;
  }

  static CustomerId customerId() {
    return CustomerId.of(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @GetMapping
  QueryListResponse getPayments(String filters, Pageable pageable);

  @GetMapping("/{orderId}")
  QueryResponse getPayment(@PathVariable String orderId);

  record QueryListResponse(List<Map<String, Object>> records, Long totalCount) {

  }

  record QueryResponse(Map<String, Object> record) {

  }

}
