package br.com.alura;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {

                for (var i = 0; i < 10; i++) {

                    var subject = "Thank you!";
                    var body = "Thank you for order! We are processing your order!";
                    var address = Math.random() + "@email.com";

                    var email = new Email(subject, body, address);

                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);

                    var order = new Order(orderId, amount, address);

                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", address, order);

                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", address, email);
                }
            }
        }
    }

}
