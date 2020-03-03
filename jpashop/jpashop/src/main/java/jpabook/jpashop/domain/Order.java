package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;


    // 다 대 일
    // 연관관계 주인은 그대로 두면 된다.
    // 다른 멤버로 변경이 된다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") // 외래키임
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // cascade하면 order안에 멤버 deliver를 셋팅해주면
    // persiste delivery안해도
    // persist order만 해줘도 알아서 persist delivery가 됨
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    // 연관관계 주인
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;


    // java 8에서는 LocalDataTime쓰면
    // hibernate가 지원해준다.
    private LocalDateTime orderDate;

    private OrderStatus status; // 주문상태 {ORDER, CANCEL}

    //==연관관계 메서드==//
    // 연관관계 주인이 하는게 좋다.
    public void setMember(Member member) {
        // 하나로 묶는 메서드를 만든다.
        this.member = member;
        member.getOrders().add(this);
        // 아래 4줄이 이렇게 된다.

    }
//    public static void main(String[] args) {
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
//    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }





}
