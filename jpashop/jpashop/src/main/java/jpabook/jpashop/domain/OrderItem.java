package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    private Long Id;
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    // 이걸 넣어줘야한다 왜냐면
    // 하나의 Order가 여러개의 OrderITem을 가질 수 있고
    // 반대로 하나의 OrderImem은 하나의 Order만 가질 수 있다.
    // member Orders와 또ㄱ같이 양방향
    @JoinColumn(name = "order_id")
    private Order order;

    // 테이블이는 Order_id (FK), Item_id (FK)

    private int orderPrice; // 주문 가격
    private int count; // 주문수량

}
