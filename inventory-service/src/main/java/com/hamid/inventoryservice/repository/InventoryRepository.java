package com.hamid.inventoryservice.repository;

import com.hamid.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  boolean existsByCode(String code);
}
