package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    @Query(value = "select b from Categories b where b.name like %:filter%")
    Page<Categories> filterQuery(String filter, Pageable pageable);

}
