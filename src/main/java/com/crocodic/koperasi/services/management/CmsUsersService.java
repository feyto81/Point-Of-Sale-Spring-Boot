package com.crocodic.koperasi.services.management;

import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsUsers;
import com.crocodic.koperasi.repository.management.CmsUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CmsUsersService {

    @Autowired
    private CmsUsersRepository repo;

    public List<CmsUsers> listAll() {
        return repo.findAll();
    }

    public List<CmsUsers> listAl() {
        return repo.findAll();
    }

    public void save(CmsUsers product) {
        repo.save(product);
    }

    public CmsUsers find(Long id) {
        Optional<CmsUsers> data = repo.findById(id);
        return data.orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void deleteCmsUsersByCmsPrivileges(CmsPrivileges cmsPrivileges){
        repo.deleteCmsUsersByCmsPrivileges(cmsPrivileges);
    }

    public Page<CmsUsers> findAllPaginate(String filter, Pageable pageable){
        return repo.filterQuery(
                filter,pageable
        );
    }

    public CmsUsers findFirstByEmail(String email){
        return repo.findByEmail(email);
    }

}
