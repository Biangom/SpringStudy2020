package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    // 중간테이블 맵핑을 해줘야한다.
    // 관계형 DB는 컬렉션 관계를 양쪽에 가질 수 있는게 아니기 때문에
    // 1대 다, 다 대 1로 풀어낼 수 있어야한다.
    // (실무 주의!!) 실무에서는 이렇게 안쓸라한다. 중간에 날짜도 넣어야하고 뭐도 해야하는데

    @JoinTable(name = "category_item",
    // 중간 테이블에 있는 카테고리 id
    joinColumns = @JoinColumn(name = "category_id"),
    // category_item테이블에 item쪽에 들어가는 것
    inverseJoinColumns = @JoinColumn(name = "item_id"))


    private List<Item> items = new ArrayList<>();

    // 부모는 누구지 자식은 누구지
    @ManyToOne // 부모니까 당연히 매니 투우ㅝㄴ
    @JoinColumn(name = "parent_id")
    private Category parent;
    // 이름은 내꺼지 다른 Entity로 하는거와 같다
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


}
