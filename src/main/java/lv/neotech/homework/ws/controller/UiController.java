package lv.neotech.homework.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/**")
public class UiController {

    @RequestMapping("/")
    public String index() {
        return "/html/index.html";
    }

}
