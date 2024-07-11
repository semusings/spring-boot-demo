package dev.bhuwanupadhyay.demo;

import dev.openfga.autoconfigure.OpenFgaProperties.Credentials;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private Springdoc springdoc;
    private Openfga openfga;

    public Springdoc getSpringdoc() {
        return springdoc;
    }

    public void setSpringdoc(Springdoc springdoc) {
        this.springdoc = springdoc;
    }

    public Openfga getOpenfga() {
        return openfga;
    }

    public void setOpenfga(Openfga openfga) {
        this.openfga = openfga;
    }

    public record Openfga(String apiUrl, String storeName, Credentials credentials) {}

    public record Springdoc(
            String title,
            String description,
            String version,
            SpringdocLicense license,
            SpringdocExternalDoc externalDoc) {}

    public record SpringdocLicense(String name, String url) {}

    public record SpringdocExternalDoc(String description, String url) {}
}
