package com.hamid.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.hamid.orderservice.dto.InventoryResponse;
import com.hamid.orderservice.dto.OrderLineItemDto;
import com.hamid.orderservice.dto.OrderRequest;
import com.hamid.orderservice.event.OrderPlacedEvent;
import com.hamid.orderservice.model.Order;
import com.hamid.orderservice.model.OrderLineItem;
import com.hamid.orderservice.repository.OrderRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;
  private final Tracer tracer;
  private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

  public String placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDtoList().stream()
        .map(this::mapToDto).toList();

    order.setOrderLineItemList(orderLineItems);

    List<String> orderItemCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getCode)
        .toList();

    log.info("Calling inventory service");

    Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

    try(Tracer.SpanInScope spanInScope = tracer.withSpanInScope(inventoryServiceLookup.start())){
      InventoryResponse[] inventoryResponse = webClientBuilder.build().get()
          .uri("http://inventory-service/api/inventory",
              uriBuilder -> uriBuilder.queryParam("code", orderItemCodes).build())
          .retrieve()
          .bodyToMono(InventoryResponse[].class)
          .block();

      boolean allAvailableInInventory = Arrays.stream(inventoryResponse)
          .allMatch(InventoryResponse::isInStock);

      if (allAvailableInInventory) {
        orderRepository.save(order);
        log.info("Order {} saved", order.getId());

        log.info("Sending message to kafka");
        kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
        log.info("message sent successfully!");

        return "Order placed successfully";
      } else {
        throw new IllegalArgumentException("Product is not in stock, please try again later");
      }
    }finally {
      inventoryServiceLookup.finish();
    }
  }

  private OrderLineItem mapToDto(OrderLineItemDto dto) {
    OrderLineItem orderLineItem = new OrderLineItem();
    orderLineItem.setPrice(dto.getPrice());
    orderLineItem.setQuantity(dto.getQuantity());
    orderLineItem.setCode(dto.getCode());
    return orderLineItem;
  }
}
