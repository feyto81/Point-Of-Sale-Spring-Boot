package com.crocodic.koperasi.controller;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@PreAuthorize("isAuthenticated()")
public class HomeController {

    private Helpers help = new Helpers();


    @GetMapping(AdminUrlString.DENIED)
    private String denaid(
            RedirectAttributes attr
    )throws Exception{
        help.message(attr,"Anda tidak mendapatkan akses ke halaman ini","warning");
        return "redirect:"+AdminUrlString.HOME;
    }

    @RequestMapping(value = AdminUrlString.HOME, method = RequestMethod.GET)
    public String home(
            Model model,
            HttpServletRequest request,
            HttpSession session,
            RedirectAttributes attr
    ) throws Exception {
        model.addAttribute("page_title", "Dashboard");
        return help.views("home/index");
    }
}
