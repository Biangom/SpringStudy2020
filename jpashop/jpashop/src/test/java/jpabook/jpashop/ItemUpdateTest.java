package jpabook.jpashop;


import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updataTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        // TX
        // 트래
        book.setName("asdfasdf");
        // 변경분에 대해서 JPA가 찾아서
        // 업데이트 쿼리를 해서 자동으로 반영을 하게함.
        // 그게 변경감지 = dirty checking

        // TX coommit





    }
}
