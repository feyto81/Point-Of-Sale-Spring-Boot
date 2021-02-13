package com.crocodic.koperasi.controller;


import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(AdminUrlString.LOGIN)
public class LoginController {

    private Helpers help = new Helpers();

    @GetMapping("")
    private String getLogin(
            HttpSession session,
            RedirectAttributes attr,
            Model data
    ) throws Exception {
        if (session.getAttribute("id_users") != null) {
            return "redirect:" + AdminUrlString.HOME;
        }

        data.addAttribute("page_title", "Login");
        return help.views("login/login");
    }
}
