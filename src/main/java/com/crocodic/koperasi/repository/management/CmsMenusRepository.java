package com.crocodic.koperasi.repository.management;


import com.crocodic.koperasi.models.management.CmsMenus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CmsMenusRepository extends JpaRepository<CmsMenus, Long> {
    @Query( "select b from CmsMenus b where b.name like %:filter% or b.icon like %:filter%")
    Page<CmsMenus> filterQuery(String filter, Pageable pageable);

    CmsMenus findByPath(String path);

    @Query( "select b from CmsMenus b where b.id=:id and b.parentId=:parenId")
    CmsMenus findAllByIdAndParentIdOrderBySortingAsc(Long id, Long parenId);

    List<CmsMenus> findAllByParentId(Long parentId, Sort sort);
}
