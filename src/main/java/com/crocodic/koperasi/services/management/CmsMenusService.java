package com.crocodic.koperasi.services.management;

import com.crocodic.koperasi.models.management.CmsMenus;
import com.crocodic.koperasi.models.management.RoleMenus;
import com.crocodic.koperasi.repository.management.CmsMenusRepository;
import com.crocodic.koperasi.string.AdminUrlString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CmsMenusService {

    @Autowired
    private CmsMenusRepository repo;

    public List<CmsMenus> listAll() {
        return repo.findAll();
    }

    public void save(CmsMenus product) {
        repo.save(product);
    }

    public CmsMenus find(Long id) {
        Optional<CmsMenus> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Page<CmsMenus> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

    public  List<RoleMenus> allMenusList()
    {
        RoleMenus r;
        RoleMenus pr;
        List<RoleMenus> menusList = new ArrayList<>();
        Long parrent = Long.parseLong("0");
        Sort sort = Sort.by(Sort.Direction.ASC,"sorting");
        List<CmsMenus> cmsMenus = repo.findAllByParentId(parrent,sort);
        for(CmsMenus m : cmsMenus){
            List<RoleMenus> parrentL = new ArrayList<>();
            r = new RoleMenus();
            List<CmsMenus> parrentList = repo.findAllByParentId(m.getId(),sort);
            for (CmsMenus p : parrentList){
                pr = new RoleMenus();
                pr.setId(p.getId());
                pr.setName(p.getName());
                pr.setIcon(p.getIcon());
                pr.setPath(AdminUrlString.adminUrl+"/"+p.getPath());
                parrentL.add(pr);
            }
            r.setId(m.getId());
            r.setName(m.getName());
            r.setIcon(m.getIcon());
            r.setPath(AdminUrlString.adminUrl+"/"+m.getPath());
            r.setRoleMenus(parrentL);
            menusList.add(r);
        }
        return menusList;
    }

}
