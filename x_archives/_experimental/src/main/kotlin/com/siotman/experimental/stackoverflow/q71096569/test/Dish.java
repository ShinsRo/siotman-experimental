package com.siotman.experimental.stackoverflow.q71096569.test;

import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "dish")
    @ToString.Exclude
    private List<OrderItem> orderItems;
}



