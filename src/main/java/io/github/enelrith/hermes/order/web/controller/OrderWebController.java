package io.github.enelrith.hermes.order.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {

    @GetMapping("/order-success")
    public String orderSuccess(){
        return "order-success";
    }

    @GetMapping("order-fail")
    public String orderFail(){
        return "order-fail";
    }
}
