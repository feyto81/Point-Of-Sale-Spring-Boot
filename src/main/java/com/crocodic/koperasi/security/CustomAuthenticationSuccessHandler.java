package com.crocodic.koperasi.security;

import com.crocodic.koperasi.helpers.Helpers;
import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.CmsPrivilegesRoles;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.models.management.RoleMenus;
import com.crocodic.koperasi.repository.management.CmsMenusRepository;
import com.crocodic.koperasi.repository.management.CmsPrivilegesRolesRepository;
import com.crocodic.koperasi.repository.management.CmsUsersRepository;
import com.crocodic.koperasi.services.management.CmsPrivilegesServices;
import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CmsPrivilegesRolesRepository cmsPrivilegesRolesRepository;
    @Autowired
    private CmsMenusRepository cmsMenusRepository;
    @Autowired
    private CmsPrivilegesServices cmsPrivilegesServices;
    @Autowired
    private CmsUsersRepository cmsUsersRepository;
    Helpers helpers = new Helpers();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        HttpSession session = request.getSession();
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailUsers = authUser.getUsername();
        CmsUsers users = cmsUsersRepository.findByEmail(emailUsers);
        String image = helpers.url(request,users.getFoto());
        session.setAttribute("id_users",users.getId());
        session.setAttribute("name",users.getName());
        session.setAttribute("email",users.getEmail());
        session.setAttribute("cms_privileges",users.getCmsPrivileges());
        session.setAttribute("role_id",users.getCmsPrivileges().getId());
        session.setAttribute("role_name",users.getCmsPrivileges().getName());
        session.setAttribute("is_superadmin",users.getCmsPrivileges().getIsSuperadmin());
        session.setAttribute("image_users",image);



        List<RoleMenus> list = new ArrayList<>();
        try {
            List<CmsPrivilegesRoles> cmsPrivilegesRoleList = cmsPrivilegesRolesRepository.findAllByCmsPrivilegesAndCanView(users.getCmsPrivileges());
            if(!cmsPrivilegesRoleList.isEmpty()){
                RoleMenus r;
                Sort sort = Sort.by(Sort.Direction.ASC, "sorting");
                for (CmsPrivilegesRoles cms : cmsPrivilegesRoleList){
                    if (cms.getCmsMenus()!=null){
                        //CmsMenus cmsMenus = cmsMenusRepository.findAllByIdAndParentIdOrderBySortingAsc(cms.getCmsMenus().getId(),Long.parseLong("0"));
                        CmsMenus cmsMenus = cms.getCmsMenus();
                        if (cmsMenus!=null){
                            if (cmsMenus.getParentId()==0) {
                                r = new RoleMenus();
                                r.setName(cmsMenus.getName());
                                r.setIcon(cmsMenus.getIcon());
                                r.setPath(AdminUrlString.adminUrl + "/" + cmsMenus.getPath());
                                List<RoleMenus> listChild = new ArrayList<>();
                                RoleMenus child;
                                for (CmsPrivilegesRoles cmsChild : cmsPrivilegesRoleList) {
                                    // CmsMenus cmsMenuCHild = cmsMenusRepository.findAllByIdAndParentIdOrderBySortingAsc(cmsChild.getCmsMenus().getId(),cmsMenus.getId());
                                    CmsMenus cmsMenuCHild = cmsChild.getCmsMenus();
                                    if (cmsMenuCHild != null) {
                                        if (cmsMenuCHild.getParentId().equals(cmsMenus.getId())) {
                                            child = new RoleMenus();
                                            child.setName(cmsMenuCHild.getName());
                                            child.setIcon(cmsMenuCHild.getIcon());
                                            child.setPath(AdminUrlString.adminUrl + "/" + cmsMenuCHild.getPath());
                                            listChild.add(child);
                                        }
                                    }
                                }
                                r.setRoleMenus(listChild);
                                list.add(r);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        session.setAttribute("listMenus",list);


        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("home?login=true");
    }

}
