package cz.ivosahlik.ecommerce.controller;

import cz.ivosahlik.ecommerce.model.BrandResponse;
import cz.ivosahlik.ecommerce.model.ProductResponse;
import cz.ivosahlik.ecommerce.model.TypeResponse;
import cz.ivosahlik.ecommerce.service.BrandService;
import cz.ivosahlik.ecommerce.service.ProductService;
import cz.ivosahlik.ecommerce.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final TypeService typeService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Integer productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(productResponse, OK);
    }

    @GetMapping()
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "brandId", required = false) Integer brandId,
            @RequestParam(name = "typeId", required = false) Integer typeId,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable = getPageable(page, size, sort, order);
        Page<ProductResponse> productResponses = productService.getProducts(pageable, brandId, typeId, keyword);
        return new ResponseEntity<>(productResponses, OK);
    }

    private Pageable getPageable(int page, int size, String sort, String order) {
        Direction direction = order.equalsIgnoreCase("desc") ? DESC : ASC;
        Sort sorting = Sort.by(direction, sort);
        return PageRequest.of(page, size, sorting);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandResponse>> getBrands() {
        List<BrandResponse> brandResponses = brandService.getAllBrands();
        return new ResponseEntity<>(brandResponses, OK);
    }

    @GetMapping("/types")
    public ResponseEntity<List<TypeResponse>> getTypes() {
        List<TypeResponse> typeResponses = typeService.getAllTypes();
        return new ResponseEntity<>(typeResponses, OK);
    }
}
