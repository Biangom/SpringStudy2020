package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "member_id") // 외래키임
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    // 연관관계 주인
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // java 8에서는 LocalDataTime쓰면
    // hibernate가 지원해준다.
    private LocalDateTime orderDate;

    private OrderStatus status; // 주문상태 {ORDER, CANCEL}


}
