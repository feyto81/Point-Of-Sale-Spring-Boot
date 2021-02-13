package com.crocodic.koperasi.services.management;

import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsPrivilegesRoles;
import com.crocodic.koperasi.repository.management.CmsPrivilegesRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CmsPrivilegesRolesService {

    @Autowired
    private CmsPrivilegesRolesRepository repo;

    public List<CmsPrivilegesRoles> listAll() {
        return repo.findAll();
    }

    public void save(CmsPrivilegesRoles product) {
        repo.save(product);
    }

    public CmsPrivilegesRoles find(Long id) {
        Optional<CmsPrivilegesRoles> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void  deleteCmsPrivilegesRolesByCmsPrivileges(CmsPrivileges cmsPrivileges){
        repo.deleteCmsPrivilegesRolesByCmsPrivileges(cmsPrivileges);
    }
    public void deleteCmsPrivilegesRolesByCmsMenus(CmsMenus cmsMenus){
        repo.deleteCmsPrivilegesRolesByCmsMenus(cmsMenus);
    }

    public List<CmsPrivilegesRoles> findAllByCmsPrivileges(CmsPrivileges cmsPrivileges){
        return repo.findAllByCmsPrivileges(cmsPrivileges);
    }

    public List<CmsPrivilegesRoles> findAllByCmsPrivilegesAndCanView(CmsPrivileges cmsPrivileges){
        return repo.findAllByCmsPrivilegesAndCanView(cmsPrivileges);
    }

}
