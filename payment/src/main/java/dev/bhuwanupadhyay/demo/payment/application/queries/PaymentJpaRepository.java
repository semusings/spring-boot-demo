package dev.bhuwanupadhyay.demo.payment.application.queries;

import dev.bhuwanupadhyay.demo.payment.model.Payment;
import dev.bhuwanupadhyay.demo.payment.model.Payment.CustomerId;
import dev.bhuwanupadhyay.demo.payment.model.Payment.OrderId;
import dev.bhuwanupadhyay.demo.payment.model.Payment.PaymentId;
import dev.bhuwanupadhyay.demo.payment.model.Payment.Status;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Payment.class, idClass = PaymentId.class)
interface PaymentJpaRepository {

  static Specification<Payment> filters(CustomerId customerId, String filters) {
    return (root, query, cb) -> {
      //
      return cb.and(cb.equal(root.get("status"), Status.INITIATED));
    };
  }

  Page<Payment> findAll(Specification<Payment> spec, Pageable pageable);

  Optional<Payment> getByCustomerIdAndOrderId(CustomerId customerId, OrderId orderId);

}
