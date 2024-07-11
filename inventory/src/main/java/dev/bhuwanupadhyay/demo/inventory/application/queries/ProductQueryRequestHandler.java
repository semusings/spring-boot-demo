package dev.bhuwanupadhyay.demo.inventory.application.queries;

import dev.bhuwanupadhyay.demo.inventory.model.Product;
import dev.bhuwanupadhyay.demo.inventory.model.Product.ProductId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
class ProductQueryRequestHandler implements ProductQuery {

    private final ProductJpaRepository products;

    public ProductQueryRequestHandler(ProductJpaRepository products) {
        this.products = products;
    }

    @Override
    public QueryListResponse getProducts(String filters, Pageable pageable) {
        Page<Product> page = products.findAll(ProductJpaRepository.filters(filters), pageable);
        List<Map<String, Object>> records =
                page.stream().map(ProductQuery::toProductResponse).collect(Collectors.toList());
        return new QueryListResponse(records, page.getTotalElements());
    }

    @Override
    public QueryResponse getProduct(String productId) {
        Product product =
                products.getByProductId(ProductId.of(productId))
                        .orElseThrow(() -> new IllegalArgumentException("Not Found"));
        return new QueryResponse(ProductQuery.toProductResponse(product));
    }
}
