package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.StockIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInRepository extends JpaRepository<StockIn, Long> {

    @Query(value = "select b from StockIn b where b.detail like %:filter% or b.date like %:filter% or b.qty like %:filter% or b.items.name like %:filter% or b.suppliers.name like %:filter%")
    Page<StockIn> filterQuery(String filter, Pageable pageable);

}
