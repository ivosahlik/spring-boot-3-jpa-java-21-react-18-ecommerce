package cz.ivosahlik.ecommerce.service.impl;

import cz.ivosahlik.ecommerce.entity.Product;
import cz.ivosahlik.ecommerce.exceptions.ProductNotFoundException;
import cz.ivosahlik.ecommerce.model.ProductResponse;
import cz.ivosahlik.ecommerce.repository.ProductRepository;
import cz.ivosahlik.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductResponse getProductById(Integer productId) {
        log.info("fetching Product by Id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product doesn't exist"));
        ProductResponse productResponse = convertToProductResponse(product);
        log.info("Fetched Product by Product Id: {}", productId);
        return productResponse;
    }

    @Override
    public Page<ProductResponse> getProducts(Pageable pageable,
                                             Integer brandId,
                                             Integer typeId,
                                             String keyword) {
        Specification<Product> spec = getProductSpecification(brandId, typeId, keyword);
        return productRepository.findAll(spec, pageable).map(this::convertToProductResponse);
    }

    private static Specification<Product> getProductSpecification(Integer brandId,
                                                                  Integer typeId,
                                                                  String keyword) {
        Specification<Product> spec = Specification.where(null);

        if (brandId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId));
        }

        if (typeId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type").get("id"), typeId));
        }

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
        }
        return spec;
    }

    private ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .pictureUrl(product.getPictureUrl())
                .productBrand(product.getBrand().getName())
                .productType(product.getType().getName())
                .build();
    }
}
