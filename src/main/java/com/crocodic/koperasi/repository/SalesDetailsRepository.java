package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesDetailsRepository extends JpaRepository<SaleDetails, Long> {

    SaleDetails findBySale(String sale_id);

    @Query("SELECT p FROM SaleDetails p WHERE p.sale LIKE %?1%")
    public List<SaleDetails> findAll(String keyword);
}
