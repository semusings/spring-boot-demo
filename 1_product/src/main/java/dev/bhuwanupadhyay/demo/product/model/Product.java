package dev.bhuwanupadhyay.demo.product.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    @EmbeddedId private ProductId productId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String name;
    private String description;
    private Double retailPrice;
    private Integer availableQuantity;

    // Required for persistence layer
    protected Product() {}

    Product(String name, String description, Double retailPrice, Integer initialQuantity) {
        this.productId = new ProductId(UUID.randomUUID());
        this.name = name;
        this.description = description;
        this.retailPrice = retailPrice;
        if (initialQuantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity can not be negative value for product: " + productId);
        }
        this.availableQuantity = initialQuantity;
        this.status = availableQuantity == 0 ? Status.OUT_OF_STOCK : Status.AVAILABLE;
    }

    public void updateStock(Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity can not be negative value for product: " + productId);
        }
        if (this.availableQuantity < quantity) {
            throw new IllegalArgumentException(
                    "Updating stock quantity is greater than available quantity: " + productId);
        }
        if (this.status.equals(Status.OUT_OF_STOCK)) {
            throw new IllegalArgumentException("Already in out of stock for product:" + productId);
        }
        this.availableQuantity = this.availableQuantity - quantity;
        if (this.availableQuantity > 0) {
            this.status = Status.AVAILABLE;
        } else {
            this.status = Status.OUT_OF_STOCK;
        }
    }

    public ProductId getProductId() {
        return productId;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        AVAILABLE,
        OUT_OF_STOCK
    }

    @Embeddable
    public record ProductId(UUID productId) implements Serializable {

        public static ProductId of(String uuid) {
            return new ProductId(UUID.fromString(uuid));
        }

        public String S() {
            return productId.toString();
        }
    }
}
