package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemeberApiController {

    private final MemberService memberService;

    // 회원을 등록하는 api 만듬.
    // 등록이니까 PostMapping으로 한다.
    @PostMapping("api/v1/members")
    // @Valid는 javax는 Validation이 자동으로 됨.
    // 회원 entity를 받고나서 ->이미 존재하는 회원입니다
    // RequestBody는 뭐냐면 (xml복잡한거뺴고) json으로 한다면
    // json data를 member로 바꿔준다고 생각하면 됨.
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // 심각한문제 있음. NotEmtpy로 했지만
        // 1. 이 NotEmpty를 엔티티에다가 다 때려넣었음. -> name이 실제로 없이 insert할 수 도 있음.
        // 2. 만약 name을 username으로 바꾸어버린다면 ? -> API 스펙자체가 바뀌어버림
        // API를 호출하는 입장 (클라이언트입장)
        // 클라가 api호출했는데 api가 깨지는 거임.
        // 까보니까 member name을 username으로 수정한거임
        // 그럼 api가 동작을 안함.
        // API <-> Enttiy 1:1로 맵핑 되어있는게 문제임!
        // 결론적으로는 API 스펙을 위한 별도의 Object를 만들어야하는데
        // 그게 DTO.
        // Enttiy를 그냥 binding해서 하면 안됨.
        // API 요청스펙에 맞춰서 DTO파라미터에 받는게 좋다.
        // 실무에서는 등록 API가 하나가 아니라 여러개 일 확률이 높다.
        // 간편가입, 페이스북가입 등등.

        // 결론 : 엔티티를 외부에 노출해서 하면 잘 안됨.
        // API를 만들을때는 엔티티를 파라미터로 받지 마라.
        // 엔티티를 외부에 노출해서도 안됨.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id); // id 를 넣으면 됨
    }


    // 1번이 주는 유일한 장점은 클래스를 안만들어도 된다는 것.
    // 그게 장점이 될 수도 있는데, 잘못하면 같이 망할 수 있음..
    // 2번의 장점은 API스펙은 안바뀜.  name을 username으로 바꿔도 컴파일 오류가 남.
    // 한번 중간에 컨트롤 해주는 거임.
    // 즉 name이 넘어오는지 address만 넘어오는 지 엔티티만 보고 감이 안옴
    // api스펙을 까보지 않으면 어떤 값이 파라미터로 넘어오는지 모름.
    // 엔티티를 보고서 감이 안옴. 서비스 로직에서 오더스를 채워줄 수 있는데.
    // 그게 API스펙에서 채워주는건지 거기서 채워주는지 모름.
    // 이렇게 DTO로 받으면 aPI스펙이 NAME만 받게 되구나! 를 ㅏㅇㄹ게됨!!
    // API스펙에 맞게 DTO에다가 NotEmpty를 넣어서 제어 가능
    // (만약 entity에다 NotEmpty를 넣으면 어떤 로직은 NotEmpty고 또다른 로직은 NotEmpty가 아닐 수도 있어서 애매모호함)
    // -> 1) 즉 DTO를 만들자!!, 2) Entity를 외부에 노출하지 말자, 3) API만들 때는 이게 정석이다!!


   @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        // 엔티티는 엔티티 용으로 쓰는거 임!
        // 엔티티와 API스펙을
        // (실무)에서는 엔티티를 노출하거나 그대로 받는건 거의 안함. -> 나중에 크게 후회함.. (이게 강의를 듣는 이유, 쌉 강조!!!)
        //  -> 아까 말한 DTO의 장점,  엔티티를 그대로 썼을 때의 단점 때문에

       // side effect가 추측이 안됨.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        /// 별도의 DTO를 만듬.
        /// 그 이유는
        /// 등록이나 수정은 API스펙이 다다름
        /// 수정은 제한적.
        /// 별도의 리퀘스트 리스펀스를 가져가는게 좋음

        /// 수정할 때 가급적이면 변경감지
        memberService.update(id, request.getName()); // 다 끝내버림
        Member findMember = memberService.findOne(id); // 그걸 반환
        /// 이러면 유지보수성 증대가 됨.
        /// 참고로 Spring MVC로 하면 좋겠는데
        /// ㄲㅂ 최대한 단순히하ㅔㅅ음

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    /// 엔티티에서는 @를 쓰는거 자제하고
    /// DTO는 @쓰는걸 막씀.
    /// 일단은 이렇게 만들긴했음
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    // id값을 반환한다.
    @Data
    static class CreateMemberResponse {
        private Long id;
        private String str = "PRINT_TEST";

        public CreateMemberResponse(Long id) {
            this.id = id;
            this.str = str;
        }
    }
}
