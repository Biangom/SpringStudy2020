package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // 이거 쓰기로 했다
public class ItemRepository {

    // spring data JPA가 해주는다.
    // 미래에는 spring이 해줄거임..
    private final EntityManager em;

    // item은 저장하기 전까지 Id값이 없다
    // 그게 없다는 것은 새로 생성한 객체라는 것.
    public void save(Item item) {
        if(item.getId() == null) {
            // 그래서 신규 등록하는 것이다.
            // JPA를 통해서 DB에 들어가는 거구나!
            em.persist(item);

        } else{
            // 뭔가 Id가 null아 아니네?
            // 그것은 이미 DB에 등록된 걸 어디서
            // 가져온 것이다.
            // 여기서 merge는 update와 비슷하다고 보면 된다.
            em.merge(item);
            // merge는 update 비슷한 것
            // 자세한 건 뒤에서 할 거임.
        }
    }


    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }



}
