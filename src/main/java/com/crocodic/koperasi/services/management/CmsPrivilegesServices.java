package com.crocodic.koperasi.services.management;

import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.repository.management.CmsPrivilegesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CmsPrivilegesServices {

    @Autowired
    private CmsPrivilegesRepository repo;

    public List<CmsPrivileges> listAll() {
        return repo.findAll();
    }

    public void save(CmsPrivileges product) {
        repo.save(product);
    }

    public CmsPrivileges find(Long id) {
        Optional<CmsPrivileges> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


    public Page<CmsPrivileges> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(filter,pageable);
    }

}
