package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {

    @Query(value = "select b from Items b where b.barcode like %:filter% or b.name like %:filter% or b.categories.name like %:filter% or b.units.name like %:filter%")
    Page<Items> filterQuery(String filter, Pageable pageable);

    Items findByBarcode(String barcode);

}
