package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // (2) 이렇게하면 (1)을 줄일 수 있음
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    // 이걸 넣어줘야한다 왜냐면
    // 하나의 Order가 여러개의 OrderITem을 가질 수 있고
    // 반대로 하나의 OrderImem은 하나의 Order만 가질 수 있다.
    // member Orders와 또ㄱ같이 양방향
    @JoinColumn(name = "order_id")
    private Order order;

    // 테이블이는 Order_id (FK), Item_id (FK)

    private int orderPrice; // 주문 가격
    private int count; // 주문수량

    // (1)
    // protected로 하면 제약을 걸 수 있음
    // (JPA에서 protected면 쓰지 말라는 얘기)
//    protected OrderItem() {
//
//    }

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        // 얼마에 샀어
        // price가 바뀔 수 있으니 price안쓴다.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);


        // 주문 상품 (OrderItem)을 만들면서
        // 재고를 하나 까준다.
        // 즉 생성할 때 재고를 까고 가준다.
        item.removeStock(count);
        return orderItem;
    }


    //==비즈니스 로직==//
    public void cancel() {
        // item의 재고를 늘리는게 목표
        // 재고 수량을 원복한다.
        System.out.println("****************** you here? 2");
        getItem().addStock(count);
    }

    //==조회 로직==//
    public int getTotalPrice() {
        // 주문 가격과 수량을 곱하면 된다.
        return getOrderPrice() * getCount();
    }




}
