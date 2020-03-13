package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
// 전략을 잡아야한다. 상속할 때는
// Joined : 는 가장 정규화된 스타일
// 싱글테이블 : 한테이블에 다 때려 박는것
// 테이블 퍼 클래스 : book movie album 만 나오는 것
public abstract class Item {
    // (1)
    // stockQuantity를 변경하려면
    // setter를 쓰는게 아니라
    // 그 비즈니스 로직을 통해서 변경하는 것이 더 좋다! (영한쌤)


    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//

    /**
     * stock 증가
     * @param quantity 123123
     */
    public void addStock(int quantity) {
        this.stockQuantity = quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if( restStock < 0 ) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }



}
