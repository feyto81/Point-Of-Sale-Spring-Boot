package com.crocodic.koperasi.repository.management;

import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsPrivilegesRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CmsPrivilegesRolesRepository extends JpaRepository<CmsPrivilegesRoles,Long> {

    CmsPrivilegesRoles findByCmsPrivilegesAndCmsMenus(CmsPrivileges cmsPrivileges, CmsMenus cmsMenus);

    void deleteCmsPrivilegesRolesByCmsPrivileges(CmsPrivileges cmsPrivileges);

    void deleteCmsPrivilegesRolesByCmsMenus(CmsMenus cmsMenus);


    @Query(value = "select c from CmsPrivilegesRoles c where c.cmsPrivileges=:cmsPrivileges and c.can_view=1 order by c.cmsMenus.sorting")
    List<CmsPrivilegesRoles> findAllByCmsPrivilegesAndCanView(CmsPrivileges cmsPrivileges);

    List<CmsPrivilegesRoles> findAllByCmsPrivileges(CmsPrivileges cmsPrivileges);

}
