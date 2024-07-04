package dev.bhuwanupadhyay.demo.inventory.application.security;


import dev.bhuwanupadhyay.demo.inventory.model.ProductPermission;
import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientTupleKey;
import dev.openfga.sdk.api.client.model.ClientWriteResponse;
import dev.openfga.sdk.errors.FgaInvalidParameterException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements ProductPermission {

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
          ._object("inventory:" + permission.product());

      ClientWriteResponse response = fgaClient.writeTuples(List.of(tuple)).get();

      if (response.getStatusCode() >= 300) {
        throw new AuthorizationException("Unable to write permission tuples");
      }

    } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
      throw new AuthorizationException("Unexpected error", e);
    }
  }

}