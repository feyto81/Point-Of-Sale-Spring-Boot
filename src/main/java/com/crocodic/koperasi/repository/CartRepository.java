package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Carts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Carts, Long> {

    @Query(value = "select b from Carts b where b.price like %:filter%")
    Page<Carts> filterQuery(String filter, Pageable pageable);

    Carts findByItem(String item_id);
}
