package org.dikshit.videochat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
//        return "index";
        return "redirect:/index.html";
    }

    @GetMapping("/admin")
    public String admin() {
//        return "admin";
        return "redirect:/admin.html";
    }
}
