package com.siotman.experimental.stackoverflow.q71096569.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private int quantity;
}