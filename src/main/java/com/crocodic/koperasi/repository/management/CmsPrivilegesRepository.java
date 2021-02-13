package com.crocodic.koperasi.repository.management;

import com.crocodic.koperasi.models.management.CmsPrivileges;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CmsPrivilegesRepository extends JpaRepository<CmsPrivileges,Long> {

    @Query(value = "select b from CmsPrivileges b where b.name like %:filter%")
    Page<CmsPrivileges> filterQuery(String filter, Pageable pageable);

}
