package com.dongnv.paygent.controller.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*")
@Controller
public class PaymentController {


    @GetMapping("/payment")
    public String sayHello() {
        return "payment";
    }

}
