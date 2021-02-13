package com.crocodic.koperasi.security;

import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.services.management.CmsMenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private CmsMenusService cmsMenusService;
    @Override
    public void onLogoutSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication
    ) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
            try {
                HttpSession session = httpServletRequest.getSession();
                session.removeAttribute("id_users");
                session.removeAttribute("name");
                session.removeAttribute("email");
                session.removeAttribute("cms_privileges");
                session.removeAttribute("role_id");
                session.removeAttribute("role_name");
                session.removeAttribute("is_superadmin");
                session.removeAttribute("foto");
                session.removeAttribute("roles_session");
                session.removeAttribute("listMenus");
                List<CmsMenus> cmsMenus  =  cmsMenusService.listAll();
                for(CmsMenus m : cmsMenus){
                    session.removeAttribute(m.getPath());
                }
                //you can add more codes here when the user successfully logs out,
                //such as updating the database for last active.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.sendRedirect("login");
    };

}
