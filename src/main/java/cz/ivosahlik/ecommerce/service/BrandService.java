package cz.ivosahlik.ecommerce.service;

import cz.ivosahlik.ecommerce.model.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getAllBrands();
}
