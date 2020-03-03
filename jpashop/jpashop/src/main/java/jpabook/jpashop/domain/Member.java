package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    // Genereted value는 시퀀스값
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 어디에 내장될거라는걸 알림
    @Embedded
    private Address address;

    // order테이블에 있는 member필드에 대해서 맵핑 된거야!
    // 매핑된 것일 뿐이야
    // Order가 member의 연관관계 주인이다!
    // 즉 여기서 값을 넣는다고해도 forenkey값이 바뀌지 않는다.

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


}
