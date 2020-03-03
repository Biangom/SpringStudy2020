package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    // 원 투 원에선 근심과 걱정ㅇ ㅣ이다.
    // order해서도 해줘야함
    // mappedBY 받은게 연관관계 노예
    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // READY는 배송준비 , COMP는 배송
    // EnumType이 ORDINAL, STRING 임
    // 인데 중간에 READY, XXX, COMP
    // 하면 망함, access 장애남
    // 그래서 STring으로 꼭 넣어줘야한다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP

}
