package com.crocodic.koperasi.repository;


import com.crocodic.koperasi.models.PembelianDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PembelianDetailsRepository extends JpaRepository<PembelianDetails, Long> {

    @Query("SELECT p FROM PembelianDetails p WHERE p.pembelian LIKE %?1%")
    public List<PembelianDetails> findAll(String keyword);
}
