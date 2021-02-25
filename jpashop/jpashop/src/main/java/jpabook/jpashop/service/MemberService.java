package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 2개 있다. Srping에 제공하는 어노테이션 쓰는게 좋다.
// 데이터 변경하는 건 Transactional이 있어야함
//@AllArgsConstructor 생성자를 만들어줌!
@RequiredArgsConstructor //얘는 final 키워드가 있는애만 생성자 만들어줌!
public class MemberService {

    // injection이 된다. fielid 인젝션이라함
    // 이게 단점이 많다.
    // memberRepository 를 못ㅅ바꾼다.
    //// final로 하는게 좋다
    //// 생성자에서 초기화 하게끔 하기 위해서!
    private final MemberRepository memberRepository;

//    // (위 내용 따라서) 따라서 아래는 장점이 있다.
//    // 테스트 코드에 가짜 memberRepo를 주입할 수 있다!
//    // 하지만 런타임 시점에 누군가 바꿀 수 있다.
//    // 개발하는 중간에 setmemberRepo 호출해서 바꾸려고할까? 없다
//    // 앱 로딩시점에 조립이 다 끝나버린다.
//    // memberService는 meberRepo를 쓰고... 이미 다 끝난다.
//    // 셋팅 조립이 끝난다
//    // 조립 후 잘 동작하는데 바꿀 일이 없다
//    // 그래서 setter 인젝션이 안좋다
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    // 그래서 궁극적으로 이게좋다 생성자 인젝션
//    // 생성자 인젝션
//    //// @AllArgsConstructor 를 클래스 위에 추가하면
//    //// 이 아래것을 만든다!
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        // 생성할때 끝나기 떄문에
//        // 중간에 set해서 바뀌지 않는다.
//        // 또 좋은게
//        // testcase작성할 때
//        // 직접 주입을 해야한다! 1-1
//        this.memberRepository = memberRepository;
//    }
//    // 요새 스프링은 생성자가 하나만 있어도 Autowired 인젝션을 해준다!
//
//    // 1-1
////    public static void main(String[] args) {
////        MemberService memberService = new MemberService(Mock());..
////    }


    // 회원 가입
    // 기본으로 false로 되어있음
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복회원이 있는지 검증하는 로직
    private void validateDuplicateMember(Member member) {
        // 여기서 위험
        // 두 회원이 동시에 이 함수를 참조하면?
        // 이러면 같은 이름 가진사람이 회원에 가입할 수 도 있다!
        // 로직이 이렇게 있다 하더라도
        // 실무에서는 멀티스레드 고려해서
        // 따라서 DB에 member name의 Unique 제약조건을 달아놓는게 좋다!
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    @Transactional(readOnly = true) // 조회할때는 readOnly해주는게 좋다 빠르다
    // 쓰기에서 넣어주면 쓰기가 안된다!
    // 더티체킹안하고..
    // 추가로 DB따라 읽기전용 트랜잭션이네?..
    // 부하 적게쓰고 읽어! (DB마다 다름
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id); // 영속성 컨텍스트 반환해줌.
        member.setName(name); // 영속상태의 member를 setName으로 바꿔주면. 얘가 종료되면 스프링이 AOP가 동작하면서
        //// @Transaction 어노테이션에 의해 @Transactional 관련 AOP가 끝나는 시점에 Transscation 커밋이 되죠

        /// 그떄 JPA가 영속성 컨텍스트를 fluh하고
        /// 데이터베이스 트랜잭션 커밋하는 거임.


        /// 김영한 선생님 스타일 : command와 쿼리를 철저히 분리한다라는 정책이 있음
        /// 이렇게하면 업데이트 하면서 쿼리를 하게됨.
        /// 업데이트는 변경성 메서드임. 얘에서 아이디를 가지고 조회하는 꼴이됨.
        /// 커멘드랑 쿼리를 같이 하게 됨.

        /// 그래서 id만 반환하거나 끝낸다.


        // 이거 블로그 포스팅하기기
    }
}

