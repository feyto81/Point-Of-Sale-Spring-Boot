package com.crocodic.koperasi.repository.management;


import com.crocodic.koperasi.models.management.CmsPrivileges;
import com.crocodic.koperasi.models.management.CmsUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CmsUsersRepository extends JpaRepository<CmsUsers,Long> {

    @Query(value = "select b from CmsUsers b where b.name like %:filter% or b.email like %:filter% or b.cmsPrivileges.name like %:filter%")
    Page<CmsUsers> filterQuery(String filter, Pageable pageable);

    CmsUsers findByEmail(String email);

    void deleteCmsUsersByCmsPrivileges(CmsPrivileges cmsPrivileges);


}
