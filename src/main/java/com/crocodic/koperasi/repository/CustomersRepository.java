package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {

    @Query(value = "select b from Customers b where b.name like %:filter% or b.address like %:filter% or b.phone like %:filter%")
    Page<Customers> filterQuery(String filter, Pageable pageable);

}
