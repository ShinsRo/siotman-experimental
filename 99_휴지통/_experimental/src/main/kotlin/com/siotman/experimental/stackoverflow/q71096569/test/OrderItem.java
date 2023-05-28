package com.siotman.experimental.stackoverflow.q71096569.test;

import lombok.ToString;

import javax.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    Dish dish;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;
    int quantity;
}