package jpabook.jpashop.service;

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


// (4)
// 하지만 테스트하려고 DB만들고 내부DB쓰거나 외부DB쓰는게 너무 귀찮다.
// 그래서 Memory DB를 쓰면 이런걸 쉽게 할 수 있다.
// test폴더에 resources만들어주자

// (4) end


@RunWith(SpringRunner.class) // 의미 Junit에 Spring랑 엮어서 실행할래
@SpringBootTest // SpringBoot를 띄운상태에서 test할래.
@Transactional // Testcase에서 사용될때는 rollback을 해준다. 하지만 Repo, servoce클래스에선 rollback을 하지 않는다.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;



     @Test
     @Rollback(false) // Rollback(false)하면? Rollback 안한다.
     // 즉 테스트 코드 실행 후 Rollback안하고 바로 적용된다!
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
    // (3)하지만 (2)부분은 더럽다
    // 이렇게 하면 (2)를 다 지울 수 있다.
    //
       @Test(expected = IllegalStateException.class)
        public void 중복_회원_예외() throws Exception {
           //given
           Member member1 = new Member();
           member1.setName("kim");

           Member member2 = new Member();
           member2.setName("kim");

           //when
           memberService.join(member1);
           // (2) 그래서 try catch 해준다
           // 즉 이렇게 해주니 아래까지 타지 않는다.
//           try {
//               memberService.join(member2); // 예외가 발생해야한다!
//           } catch (IllegalStateException e) {
//               return;
//           }
           // (2) end


           memberService.join(member2); // 예외가 발생해야한다!


           // 여기서 우리가 선언한 Illegal 예외가 빠져나오게된다
           // 저 안의 메서드들에서 try catch를 안해줬기 때문.



           //then
           //코드가 돌다가 그쪽으로 가면 안되는 부분이 fail키워드이다.
           // 즉 위에서 제어가 끝나고 밑으로 가면 안되는 것이다!
           fail("<fail> 예외가 발생 한다!!.");
        }



}