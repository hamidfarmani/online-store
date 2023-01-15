package com.hamid.inventoryservice;

import com.hamid.inventoryservice.model.Inventory;
import com.hamid.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setCode("iphone_14");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setCode("iphone_14_red");
			inventory1.setQuantity(0);


			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
