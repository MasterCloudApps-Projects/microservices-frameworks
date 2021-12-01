package io.eventuate.examples.tram.sagas.ordersandcustomers.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;

@SpringBootApplication
@Configuration
@Import({ CustomerConfiguration.class,
          TramMessageProducerJdbcConfiguration.class,
          EventuateTramKafkaMessageConsumerConfiguration.class})
public class CustomersServiceMain {

  public static void main(String[] args) {
    SpringApplication.run(CustomersServiceMain.class, args);
  }
}
