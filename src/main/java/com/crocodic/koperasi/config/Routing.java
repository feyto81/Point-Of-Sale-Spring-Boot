package com.crocodic.koperasi.config;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class Routing {

    Helpers help = new Helpers();

    @GetMapping("/")
    private String getIndex(
            HttpSession session,
            RedirectAttributes attr
    ){
        if (session.getAttribute("id_users")==null){
            help.message(attr,"Anda Harus Login Terlebih Dahulu","warning");
            return "redirect:"+ AdminUrlString.LOGIN;
        }
        return "redirect:"+ AdminUrlString.HOME;
    }


}
