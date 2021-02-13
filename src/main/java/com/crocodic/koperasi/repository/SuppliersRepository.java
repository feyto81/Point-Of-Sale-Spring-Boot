package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Suppliers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliersRepository extends JpaRepository<Suppliers, Long> {

    @Query(value = "select b from Suppliers b where b.name like %:filter% or b.address like %:filter%")
    Page<Suppliers> filterQuery(String filter, Pageable pageable);

}
