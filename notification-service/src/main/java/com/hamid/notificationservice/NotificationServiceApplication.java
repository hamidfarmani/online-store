package com.hamid.notificationservice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApplication.class, args);
  }

  @KafkaListener(id = "notificationServiceId", topics = "notificationTopic")
  public void handleNotification(OrderPlacedEvent orderPlacedEvent){
    //TODO: send an email notification
    log.info("Received Notification for order: {}", orderPlacedEvent.getOrderNumber());
  }

}
