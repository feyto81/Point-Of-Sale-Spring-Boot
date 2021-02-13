package com.crocodic.koperasi.security;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsPrivilegesRoles;
import com.crocodic.koperasi.repository.management.CmsMenusRepository;
import com.crocodic.koperasi.repository.management.CmsPrivilegesRolesRepository;
import com.crocodic.koperasi.string.AdminUrlString;
import com.crocodic.koperasi.string.ApiUrlString;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class LoggingFilter extends GenericFilterBean {

    Helpers help = new Helpers();

    @Autowired
    private CmsMenusRepository cmsMenusRepository;


    @Autowired
    private CmsPrivilegesRolesRepository cmsPrivilegesRolesRepository;


    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String url = request.getRequestURL().toString();
        final HttpServletResponse res = (HttpServletResponse) response;
        final String queryString = Optional.ofNullable(request.getQueryString()).map(value -> "?" + value).orElse("");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUrl = request.getRequestURI();

        //ADMIN LOG
        if (authentication != null) {
            if (currentUrl.contains("admin")){

                String[] path = currentUrl.split("/");
                String link = path[3];
                String contextPath = request.getContextPath();
                if(!link.contains("login") && !link.contains("logout") && !link.contains("home") && !link.contains("denied")){
                    CmsMenus menu = (CmsMenus) request.getSession().getAttribute(link);
                    if (menu==null){
                        menu = cmsMenusRepository.findByPath(link);
                        request.getSession().setAttribute(link,menu);
                    }
                    if(menu!=null){
                        CmsPrivilegesRoles roles = (CmsPrivilegesRoles) request.getSession().getAttribute("roles_session");
                        if (roles==null){
                            CmsPrivileges cmsPrivileges = (CmsPrivileges) request.getSession().getAttribute("cms_privileges");
                            roles = cmsPrivilegesRolesRepository.findByCmsPrivilegesAndCmsMenus(cmsPrivileges,menu);
                            request.getSession().setAttribute("roles_session",roles);
                        }

                        if (roles!=null){
                            String addLink = link+"/add";
                            String editLink = link+"/edit";
                            String deleteLink = link+"/delete";
                            String deleteBulkLink = link+"/action-selected";
                            if(currentUrl.contains(addLink)){
                                if (roles.getCan_add()!=1){
                                    ((HttpServletResponse) response).sendRedirect(contextPath+ AdminUrlString.DENIED);
                                    return;
                                }
                            }else if (currentUrl.contains(editLink)){
                                if (roles.getCan_edit()!=1){
                                    ((HttpServletResponse) response).sendRedirect(contextPath+AdminUrlString.DENIED);
                                    return;
                                }
                            }else if (currentUrl.contains(deleteLink)){
                                if (roles.getCan_add()!=1){
                                    ((HttpServletResponse) response).sendRedirect(contextPath+AdminUrlString.DENIED);
                                    return;
                                }
                            }else if (currentUrl.contains(deleteBulkLink)){
                                if (roles.getCan_add()!=1){
                                    ((HttpServletResponse) response).sendRedirect(contextPath+AdminUrlString.DENIED);
                                    return;
                                }
                            }else{
                                if (roles.getCan_view()!=1){
                                    ((HttpServletResponse) response).sendRedirect(contextPath+AdminUrlString.DENIED);
                                    return;
                                }
                            }
                        }
                        else{
                            ((HttpServletResponse) response).sendRedirect(contextPath+AdminUrlString.DENIED);
                            return;
                        }
                    }
//                    else{
//                        ((HttpServletResponse) response).sendRedirect("404");
//                        return ;
//                    }
                }
            }
        }



        //API TOKEN AKSES
        if (currentUrl.contains(ApiUrlString.apiBase)) {

            if (!currentUrl.contains(ApiUrlString.getTokenFull) && !currentUrl.contains(ApiUrlString.apiToolsBase) && !currentUrl.contains(ApiUrlString.renewTokenFull)) {
                // logic for token
            }
        }

        filterChain.doFilter(servletRequest, response);
    }
}
