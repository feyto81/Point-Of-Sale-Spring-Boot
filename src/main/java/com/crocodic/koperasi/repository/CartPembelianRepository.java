package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.CartPembelian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartPembelianRepository extends JpaRepository<CartPembelian, Long> {

    @Query(value = "select b from CartPembelian b where b.createdAt like %:filter%")
    Page<CartPembelian> filterQuery(String filter, Pageable pageable);

    CartPembelian findByItem(String item_id);
}
