package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;



     @Test
     @Rollback(false)
      public void 회원가입() throws Exception {
         // 머리, 가슴, 배로 나눠서 해놓는게 좋다!
         //given
         Member member = new Member();
         member.setName("kim");

         //when
         Long saveId = memberService.join(member);
         // 커밋을 하는 순간
         // JPA 영속성 컨텍스트에 있는 저 객체가 만들어지면서
         // DB insert문이 나간다!
         // Spring에서 Trasactnional에서는 기본적으로 커밋을 안하고 롤백함.
         // 그래서 롤백을 false로 줘야함.
         // 그래야 insert문 나감.
         // rollback하면 영속성 컨텍스트가 안나감!





         //then
         // 롤백 true하고 테스트해보고싶다? 그럼 아래코드를 삽입한다.
         // 하면 영속성컨텍스트가 반영됨!
         // em.flush();

         // member와 saveID와 같으면 같다고 나올 것이다.
         // 가능한 이유는 같은 Transactional, iid값이 같으면 같은 영속성 컨텍스트에서 하나로 관리가 된다.
         assertEquals(member, memberRepository.findOne(saveId));
      }
      // 회원가입 (녹색) 뜨면 OK이 된거!


    // 중복 로직 검증
       @Test
        public void 중복_회원_예외() throws Exception {
           //given
           Member member1 = new Member();
           member1.setName("kim");

           Member member2 = new Member();
           member2.setName("kim");

           //when
           memberService.join(member1);
           memberService.join(member2); // 예외가 발생해야한다!
           



           //then
        }



}