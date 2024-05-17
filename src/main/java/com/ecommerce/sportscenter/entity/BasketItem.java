package com.ecommerce.sportscenter.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Setter
@RedisHash("BasketItem")
public class BasketItem {
    @Id
    private Integer id;
    private String name;
    private String description;
    private Long price;
    private String pictureUrl;
    private String productBrand;
    private String productType;
    private Integer quantity;
}
