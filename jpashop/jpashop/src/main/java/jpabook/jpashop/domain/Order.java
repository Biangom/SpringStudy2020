package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order  {
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

    //== 생성 메서드==//
    // order만 생성할 게 아니라
    // orderItem, delivery 등등
    // 여러 연관관계도 들어가고 복잡해진다.
    // 복잡한 생성은 별도의 생성 메서드가 있는게 좋다.
    // 이런 스타일이 왜 중요하냐면
    // 생성하는 부분만 수정할 때 이 메서드만 수정하면 되어서 편하다
    // (1) 주문 생성에 대한 복잡한 비즈니스 로직을 완결을 시킴.
    // 주문을 생성하는거 관련하는 거는 여기에만 고치면 됨.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        // 위에 파라미터처럼 생성할 수도 있고
        // OrderItem 만들어서 createOrder할 수도 있다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // order status를 처음 상태로 강제할 것이다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 주문 취소
     */
    // 재고 다시 올려줘야한다.. 그런 비즈니스 로직이 있다.
    public void cancel() {
        // 만약 delivery가 배송완료 상태라면
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        // 이걸 통과하면 CANCLE 상태로 바꾸면 됨.
        System.out.println("****************** you here?");
        this.setStatus(OrderStatus.CANCEL);
        System.out.println(" 1!!!!!!!!!!!!" + this.getStatus());
        for (OrderItem orderItem: orderItems ) {
            // 현재 orderItem에는 cancel 메서드가 구현되어있지않음
            // 고객이 상품 2개 주문할 수 도 있음.
            // 2개 각각에 cancel cancel 날려야한다.
            orderItem.cancel();
        }
    }

    //==조회로직==//

    /**
     * 전체 주문 가격 조회
     * @return
     */
    public int getTotalPrice() {
        // 전체 주문 가격을 만들어야한다.
        // OrderItem들을 다더하면됨
        int totalPrice = 0;
        for (OrderItem orderItem: orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        // 스트림 람다를 활용하면 깔끔하게 가능함.
        // Alt+Enter를 누르면 stream으로 깔끔하게 변경가능
        // int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        // 로 가능
        return totalPrice;
    }






}
