package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // controller가 기본적으로 service 씀
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        // model이 뭐나면
        // Controller에서 view로 넘어갈때 데이터를 실어서 넘김.
        // 빈껍데기 객체를 가지고간다.
        // 그 이유는 validation을 해주기 위해 빈껍데기 갖고감.

        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    // 회원 이름을 필수로 쓰고 싶다?
    // @Valid 키워드 쓰자.
    // (1)
    // BindingResult 선언해주면
    // 오류가 result에 담겨서 코드가 실행이됨.
    public String create(@Valid MemberForm form, BindingResult result) {

        //result를 보면 error를 위한 메시지 찾을게 많음

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        // 저장이 끝남.
        memberService.join(member);

        // 저장하고 어디로가나?
        // 저장이 되고나서 redirect하는게 좋다.

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";

    }

}
