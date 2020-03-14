package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // (1)
public class HomeController {

    // (1)을 쓰면 아랫줄 안써도됨.
    //Logger log = LoggerFactory.getLogger(getClass());

    // 첫번쨰화면으로오면 여기로 잡함.
    @RequestMapping("/")
    public String home() {
        // 이렇게하면 home.html로 찾아가서
        // thymeleaf 파일을 찾아가게됨.
        log.info("home controller");
        return "home";
    }
}
