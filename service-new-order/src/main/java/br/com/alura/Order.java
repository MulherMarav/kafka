package br.com.alura;

import java.math.BigDecimal;

public class Order {

    private final String orderId;
    private final BigDecimal amount;

    private final String email;

    public Order(final String orderId, final BigDecimal amount, final String email) {
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }

}
