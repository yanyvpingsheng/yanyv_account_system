package com.yanyv.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("login")
    public String login(HttpSession session) {
        session.setAttribute("needPictureCode", false);
        return "login";
    }

}
