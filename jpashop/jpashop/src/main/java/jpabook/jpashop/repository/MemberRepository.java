package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {


//  1-1
//    // 이렇게 해주면
//    // spring이 Entity 매니저를 만들어 주입을 해준다.
//    @PersistenceContext
//    private EntityManager em;
//
//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }


//  1-1을 1-2, 1-3로 바꿀 수 있다!
//  1-2
//    @Autowired
//    private EntityManager em;
//  1-3
    private final EntityManager em;


    // 만약 factroy를 주입받고 싶다?
    // 그럼 아래 어노테이션쓴다
    // 하지만 거의 쓸일 없다.
//    @PersistenceUnit
//    private EntityManagerFactory emf;


    public void save(Member member) {
        // JPA가 얘를 저장하는 Logic이다!
        // 영속성 컨텍스트에 member객체를 넣은 뒤
        // 트랜잭션이 커밋되는 시점에 DB에 반영이 된다.
        em.persist(member);
    }

    // 찾아서 반환해준다.
    public Member findOne(Long id) {
        // 단건 조회
        // 첫번째 : 타입
        // 두번째 : PK
        return em.find(Member.class, id);
    }

    //list 조회
    public List<Member> findAll() {
        // 전부다 찾아야한다.
        // SQL은 테이블로하는데 JQL은 엔티티 객체를 대상으로 select한다!
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
        // 두번째 인자가가 반환 타입
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        // 회원을 조회하는데 이름을 통해서 조회를 한다!


    }
}
