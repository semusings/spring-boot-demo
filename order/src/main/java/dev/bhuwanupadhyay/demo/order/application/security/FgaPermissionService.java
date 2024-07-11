package dev.bhuwanupadhyay.demo.order.application.security;

import dev.bhuwanupadhyay.demo.order.model.OrderPermission;
import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientTupleKey;
import dev.openfga.sdk.api.client.model.ClientWriteResponse;
import dev.openfga.sdk.errors.FgaError;
import dev.openfga.sdk.errors.FgaInvalidParameterException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FgaPermissionService implements OrderPermission {

    private final OpenFgaClient fgaClient;

    public FgaPermissionService(OpenFgaClient fgaClient) {
        this.fgaClient = fgaClient;
    }

    @Override
    public void assign(Permission permission) {
        try {
            ClientTupleKey tuple =
                    new ClientTupleKey() //
                            .user("customer:" + permission.customerId()) //
                            .relation(permission.relation()) //
                            ._object("order:" + permission.orderId());

            ClientWriteResponse response = fgaClient.writeTuples(List.of(tuple)).get();

            if (response.getStatusCode() >= 300) {
                throw new FgaPermissionException("Unable to write permission tuples");
            }

        } catch (FgaInvalidParameterException | InterruptedException | ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof FgaError) {
                throw new FgaPermissionException(
                        String.format(
                                "%s: %s", e.getMessage(), ((FgaError) cause).getResponseData()),
                        e);
            }
            throw new FgaPermissionException(e.getMessage(), e);
        }
    }
}
