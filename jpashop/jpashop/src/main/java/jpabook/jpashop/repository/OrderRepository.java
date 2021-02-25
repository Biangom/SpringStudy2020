package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 나중에 구현할 것
    public List<Order> findAll(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";// +
                // 아래 두줄을 지우면됨 <- 데이터 다 갖고오려면
                //" where o.status = :status " +
                //" and m.name like :name";



        // (1) 실무에선 이렇게 안함
        // 문자로 더하기하기엔 너무 복잡함.(권장하지 않는방법임 (1)은)
        // 이렇게 jpql을 문자로 생성하기엔 너무 번거롭고 실수발생하기 쉬움.
        // mybatis, ibatis 쓰는 이유가 동적퀴리를 쓰기 편하다는 이점이있음.
        boolean isFirstCondition = true;
        // 주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            }
            else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            }
            else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        // -- (1) end


        return em.createQuery(jpql, Order.class)
                //.setParameter("status", orderSearch.getOrderStatus())
                //.setParameter("name",orderSearch.getMemberName())
                //.setFirstResult(100) // 페이지 100부터 시작할거다.
                .setMaxResults(1000) // 최대 1000건 갖고오겠음.
                .getResultList();
        // JQL을 쓰자!
        // order를 조회를 하고, order랑 member랑 join을 하는 것임.
        // 실질적으로 돌려보면 join문으로 됨.
    }

    // (2)
    // JPQL를 자바쿼리로 작성할 수 있게끔
    // 표준으로 정의해준게 있음
    // JPA Criteria로 해결하는 방법.
    // JPA Criteria가 도와줌
    // (2)도 권장하는 방법이 아님.
    // 몰라도 됨 아래것은.
    // 사람이 쓰라고 만들어놓은게 아님... JPA표준 스펙이긴한데
    // 비교될 예제로 보기만하면됨
    // ★ 치명적인 단점이 있음:
    // 사실 많이 고민했음 동적쿼리 어떻게하면 좋을까.
    // 하지만 이 방식은 머리로만짠 코드느낌 (실무는 생각안하고)
    // 이 방식의 코드는 유지보수성이 거의 없음.
    // 이걸 보면 join하는지 뭐하는지 직관적으로 안떠올림.
    // 무슨 JPQL이 만들어지는지도 모름.
    // 이 스펙은 JPA 책에 있음.
    // 결론 : 지금 이코드를 Quary dsl로 처리하겠음.
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 이렇게하면 (1)방법으로 안해도됨
        // 주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 ㅣㅇ름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }


    // (3) <- 젤 나은 방법
    // Query dsl 동적쿼리에 강력히 편함. 정적쿼리에도 편함

//    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder order = QOrder.
//    }



}
