package com.hamid.orderservice.service;

import com.hamid.orderservice.dto.InventoryResponse;
import com.hamid.orderservice.dto.OrderLineItemDto;
import com.hamid.orderservice.dto.OrderRequest;
import com.hamid.orderservice.model.Order;
import com.hamid.orderservice.model.OrderLineItem;
import com.hamid.orderservice.repository.OrderRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient webClient;

  public void placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDtoList().stream()
        .map(this::mapToDto).toList();

    order.setOrderLineItemList(orderLineItems);

    List<String> orderItemCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getCode)
        .toList();

    InventoryResponse[] inventoryResponse = webClient.get()
        .uri("http://localhost:8182/api/inventory",
            uriBuilder -> uriBuilder.queryParam("code", orderItemCodes).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();

    boolean allAvailableInInventory = Arrays.stream(inventoryResponse)
        .allMatch(InventoryResponse::isInStock);

    if (allAvailableInInventory) {
      orderRepository.save(order);
    } else {
      throw new IllegalArgumentException("Product is not in stock, please try again later");
    }
    log.info("Order {} saved", order.getId());

  }

  private OrderLineItem mapToDto(OrderLineItemDto dto) {
    OrderLineItem orderLineItem = new OrderLineItem();
    orderLineItem.setPrice(dto.getPrice());
    orderLineItem.setQuantity(dto.getQuantity());
    orderLineItem.setCode(dto.getCode());
    return orderLineItem;
  }
}
