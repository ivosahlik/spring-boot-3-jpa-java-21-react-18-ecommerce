package cz.ivosahlik.ecommerce.mapper;

import cz.ivosahlik.ecommerce.entity.order_aggregate.Order;
import cz.ivosahlik.ecommerce.model.OrderDto;
import cz.ivosahlik.ecommerce.model.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "basketId", target = "basketId")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "deliveryFee", target = "deliveryFee")
    @Mapping(target = "total", expression = "java(order.getSubTotal() + order.getDeliveryFee())")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "orderStatus", constant = "PENDING")
    OrderResponse OrderToOrderResponse(Order order);

    @Mapping(target = "orderDate", expression = "java(orderDto.getOrderDate())")
    @Mapping(target = "orderStatus", constant = "PENDING")
    Order orderResponseToOrder(OrderDto orderDto);

    List<OrderDto> ordersToOrderResponses(List<Order> orders);

    void updateOrderFromOrderResponse(OrderDto orderDto, @MappingTarget Order order);
}
