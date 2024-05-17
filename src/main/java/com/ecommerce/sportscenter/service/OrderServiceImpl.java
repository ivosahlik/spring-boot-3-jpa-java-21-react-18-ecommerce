package com.ecommerce.sportscenter.service;

import com.ecommerce.sportscenter.entity.OrderAggregate.Order;
import com.ecommerce.sportscenter.entity.OrderAggregate.OrderItem;
import com.ecommerce.sportscenter.entity.OrderAggregate.ProductItemOrdered;
import com.ecommerce.sportscenter.mapper.OrderMapper;
import com.ecommerce.sportscenter.model.BasketItemResponse;
import com.ecommerce.sportscenter.model.BasketResponse;
import com.ecommerce.sportscenter.model.OrderDto;
import com.ecommerce.sportscenter.model.OrderResponse;
import com.ecommerce.sportscenter.repository.BrandRepository;
import com.ecommerce.sportscenter.repository.OrderRepository;
import com.ecommerce.sportscenter.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BasketService basketService;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse getOrderById(Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(orderMapper::OrderToOrderResponse).orElse(null);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::OrderToOrderResponse).toList();
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::OrderToOrderResponse);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Integer createOrder(OrderDto orderDto) {
        BasketResponse basketResponse = basketService.getBasketById(orderDto.getBasketId());
        if (basketResponse == null) {
            log.error("Basket with ID {} not found", orderDto.getBasketId());
            return null;
        }
        List<OrderItem> orderItems = basketResponse.getItems().stream()
                .map(this::mapBasketItemToOrderItem)
                .toList();

        double subTotal = basketResponse.getItems().stream()
                .mapToDouble(OrderServiceImpl::calculateSubTotal)
                .sum();
        Order order = orderMapper.orderResponseToOrder(orderDto);
        order.setOrderItems(orderItems);
        order.setSubTotal(subTotal);

        Order savedOrder = orderRepository.save(order);
        basketService.deleteBasketById(orderDto.getBasketId());
        return savedOrder.getId();
    }

    private static long calculateSubTotal(BasketItemResponse item) {
        return item.getPrice() * item.getQuantity();
    }

    private OrderItem mapBasketItemToOrderItem(BasketItemResponse basketItemResponse) {
        if (basketItemResponse == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setItemOrdered(mapBasketItemToProduct(basketItemResponse));
        orderItem.setQuantity(basketItemResponse.getQuantity());
        return orderItem;
    }

    private ProductItemOrdered mapBasketItemToProduct(BasketItemResponse basketItemResponse) {
        ProductItemOrdered productItemOrdered = new ProductItemOrdered();
        productItemOrdered.setName(basketItemResponse.getName());
        productItemOrdered.setPictureUrl(basketItemResponse.getPictureUrl());
        productItemOrdered.setProductId(basketItemResponse.getId());
        return productItemOrdered;
    }
}
