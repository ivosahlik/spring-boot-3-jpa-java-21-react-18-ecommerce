package cz.ivosahlik.ecommerce.service;

import cz.ivosahlik.ecommerce.entity.Basket;
import cz.ivosahlik.ecommerce.model.BasketResponse;

import java.util.List;

public interface BasketService {
    List<BasketResponse> getAllBaskets();
    BasketResponse getBasketById(String basketId);
    void deleteBasketById(String basketId);
    BasketResponse createBasket(Basket basket);
}
