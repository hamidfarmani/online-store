package com.hamid.inventoryservice.service;

import com.hamid.inventoryservice.dto.InventoryResponse;
import com.hamid.inventoryservice.repository.InventoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public List<InventoryResponse> isInStock(List<String> code){
    return inventoryRepository.findByCodeIn(code).stream()
        .map(inventory ->
          InventoryResponse.builder().code(inventory.getCode())
              .isInStock(inventory.getQuantity() > 0)
              .build()
        ).toList();
  }
}
