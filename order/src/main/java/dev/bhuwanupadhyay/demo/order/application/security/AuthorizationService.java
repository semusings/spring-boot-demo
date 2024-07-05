package dev.bhuwanupadhyay.demo.order.application.security;

import dev.bhuwanupadhyay.demo.order.model.OrderPermission;
import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientTupleKey;
import dev.openfga.sdk.api.client.model.ClientWriteResponse;
import dev.openfga.sdk.errors.FgaInvalidParameterException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements OrderPermission {

  private final OpenFgaClient fgaClient;

  public AuthorizationService(OpenFgaClient fgaClient) {
    this.fgaClient = fgaClient;
  }

  @Override
  public void assign(Permission permission) {
    try {
      ClientTupleKey tuple = new ClientTupleKey() //
          .user("user:" + permission.customerId()) //
          .relation(permission.relation()) //
          ._object("order:" + permission.orderId());

      ClientWriteResponse response = fgaClient.writeTuples(List.of(tuple)).get();

      if (response.getStatusCode() >= 300) {
        throw new AuthorizationException("Unable to write permission tuples");
      }

    } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
      throw new AuthorizationException(e.getMessage(), e);
    }
  }

}