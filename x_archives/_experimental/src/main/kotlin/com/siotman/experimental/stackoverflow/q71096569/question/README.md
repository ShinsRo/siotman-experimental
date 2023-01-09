## JPA @OneToMany and @ManyToOne: back reference is null though mappedBy is included

---
https://stackoverflow.com/questions/71096569/jpa-onetomany-and-manytoone-back-reference-is-null-though-mappedby-is-include

have 4 Entities, that a related to each other with @OneToMany relationships. When I try to save Order that contains
OrderItem - Orderitem has no backreference.

In the code below only important fields are showed for brevity ( usual strings and primitives are omitted ). I decided
to include Dish and User Entities also.

```java

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<OrderItem> orderItems;
}

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
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


@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Order> orders;
}
```

The problem happens when I try to save Order with Spring data JPA. Let's print Order to see OrderItem before saving.

```java
class Service {
    public Order saveOrder(Order order) {
        System.out.println("SERVICE saving order " + order);
        return orderRepository.save(order);
    }
}
```

As you can see, orderItems backreference is null before saving ( I though spring data jpa should deal with setting
it ).

> SERVICE saving order Order(id=0, orderItems=[OrderItem(id=0, quantity=2, order=null)])

Here is what I have in DB ( Order and OrderItem entities ). enter image description here

> order_item 에 생성된 로우에 id 필드가 null 로 셋팅된 이미지.