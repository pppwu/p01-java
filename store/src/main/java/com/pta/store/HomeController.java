package com.pta.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({"/", "/{x:[\\w\\-]+}"})
    public String index() {
        return "forward:/index.html";
    }
}
