package com.hamid.inventoryservice.repository;

import com.hamid.inventoryservice.model.Inventory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  List<Inventory> findByCodeIn(List<String> code);
}
