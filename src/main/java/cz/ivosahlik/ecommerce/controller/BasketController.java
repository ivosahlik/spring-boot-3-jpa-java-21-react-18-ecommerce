package cz.ivosahlik.ecommerce.controller;

import cz.ivosahlik.ecommerce.entity.Basket;
import cz.ivosahlik.ecommerce.entity.BasketItem;
import cz.ivosahlik.ecommerce.model.BasketItemResponse;
import cz.ivosahlik.ecommerce.model.BasketResponse;
import cz.ivosahlik.ecommerce.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService basketService;

    @GetMapping
    public List<BasketResponse> getAllBaskets() {
        return basketService.getAllBaskets();
    }
    @GetMapping("/{basketId}")
    public BasketResponse getBasketById(@PathVariable String basketId){
        return basketService.getBasketById(basketId);
    }
    @DeleteMapping("/{basketId}")
    public void deleteBasketById(@PathVariable String basketId){
        basketService.deleteBasketById(basketId);
    }

    @PostMapping
    public ResponseEntity<BasketResponse> createBasket(@RequestBody BasketResponse basketResponse){
        Basket basket = convertToBasketEntity(basketResponse);
        BasketResponse createdBasket = basketService.createBasket(basket);
        return new ResponseEntity<>(createdBasket, HttpStatus.CREATED);
    }

    private Basket convertToBasketEntity(BasketResponse basketResponse) {
        Basket basket = new Basket();
        basket.setId(basketResponse.getId());
        basket.setItems(mapBasketItemResponsesToEntities(basketResponse.getItems()));
        return basket;
    }

    private List<BasketItem> mapBasketItemResponsesToEntities(List<BasketItemResponse> itemResponses) {
        return itemResponses.stream()
                .map(this::convertToBasketItemEntity)
                .toList();
    }

    private BasketItem convertToBasketItemEntity(BasketItemResponse itemResponse) {
        return BasketItem.builder()
                .id(itemResponse.getId())
                .name(itemResponse.getName())
                .description(itemResponse.getDescription())
                .price(itemResponse.getPrice())
                .pictureUrl(itemResponse.getPictureUrl())
                .productBrand(itemResponse.getProductBrand())
                .productType(itemResponse.getProductType())
                .quantity(itemResponse.getQuantity())
                .build();
    }
}
