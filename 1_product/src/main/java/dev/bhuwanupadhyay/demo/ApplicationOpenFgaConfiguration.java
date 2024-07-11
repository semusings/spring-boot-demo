package dev.bhuwanupadhyay.demo;

import dev.openfga.autoconfigure.OpenFgaProperties;
import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.configuration.ApiToken;
import dev.openfga.sdk.api.configuration.ClientConfiguration;
import dev.openfga.sdk.api.configuration.ClientCredentials;
import dev.openfga.sdk.api.configuration.Credentials;
import dev.openfga.sdk.api.configuration.CredentialsMethod;
import dev.openfga.sdk.api.model.Store;
import dev.openfga.sdk.errors.FgaInvalidParameterException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@Configuration
public class ApplicationOpenFgaConfiguration {

    @Value("${openfga.api-url}")
    private String apiUrl;

    @Value("${openfga.store-name}")
    private String storeName;

    @Bean
    @Primary
    public ClientConfiguration fgaConfig(OpenFgaProperties openFgaProperties)
            throws FgaInvalidParameterException, ExecutionException, InterruptedException {
        var credentials = new Credentials();

        var credentialsProperties = openFgaProperties.getCredentials();

        if (credentialsProperties != null) {
            if (OpenFgaProperties.CredentialsMethod.API_TOKEN.equals(
                    credentialsProperties.getMethod())) {
                credentials.setCredentialsMethod(CredentialsMethod.API_TOKEN);
                credentials.setApiToken(
                        new ApiToken(credentialsProperties.getConfig().getApiToken()));
            } else if (OpenFgaProperties.CredentialsMethod.CLIENT_CREDENTIALS.equals(
                    credentialsProperties.getMethod())) {
                ClientCredentials clientCredentials =
                        new ClientCredentials()
                                .clientId(credentialsProperties.getConfig().getClientId())
                                .clientSecret(credentialsProperties.getConfig().getClientSecret())
                                .apiTokenIssuer(
                                        credentialsProperties.getConfig().getApiTokenIssuer())
                                .apiAudience(credentialsProperties.getConfig().getApiAudience())
                                .scopes(credentialsProperties.getConfig().getScopes());

                credentials.setCredentialsMethod(CredentialsMethod.CLIENT_CREDENTIALS);
                credentials.setClientCredentials(clientCredentials);
            }
        }

        var openFgaClient = new OpenFgaClient(new ClientConfiguration().apiUrl(apiUrl));
        var clientListStoresResponse = openFgaClient.listStores().get();
        var storeId =
                clientListStoresResponse.getStores().stream()
                        .filter(store -> Objects.equals(store.getName(), storeName))
                        .max(Comparator.comparing(Store::getCreatedAt))
                        .orElseThrow(storeNotRegistered(storeName))
                        .getId();
        var openFgaAuthorizationModelClient =
                new OpenFgaClient(new ClientConfiguration().apiUrl(apiUrl).storeId(storeId));
        var clientReadAuthorizationModelsResponse =
                openFgaAuthorizationModelClient.readLatestAuthorizationModel().get();

        return new ClientConfiguration()
                .apiUrl(apiUrl)
                .storeId(storeId)
                .authorizationModelId(
                        Objects.requireNonNull(
                                        clientReadAuthorizationModelsResponse
                                                .getAuthorizationModel())
                                .getId())
                .credentials(credentials);
    }

    private static Supplier<IllegalStateException> storeNotRegistered(String storeName) {
        return () ->
                new IllegalStateException(
                        "OpenFGA store not registered with store name: [" + storeName + "]");
    }
}
