package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    // @RequestParam은
    // form submit 방식으로 오면
    // 선택된 id의 value가 넘어옴
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        // 컨트롤러에서 안찾고 바로 서비스로 주었음.
        // 그 이유는 단순화되었기 떄문.
        // 아이디만 딱딱 넘겨서 깔끔하게 동작할 수 있음.

        // 결론, service의 order에는 memberId, itemId, count
        // 를 넘기는게 좋음 (객체를 넘기는 것 보다).
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        //저 @ModelAttribute 태그로 인해
        // 아래가 생김
        // Spring MVC하면 기본적인 것임.
        //model.addAttribute("orderSearch",orderSearch);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        System.out.println("order Id : " + orderId);
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
