package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepositoryS {

    @PersistenceContext
    EntityManager em;

    public Long save(MemberS member) {
        em.persist(member);
        return member.getId();
    }

    public MemberS find(Long id) {
        return em.find(MemberS.class, id);
    }
}
