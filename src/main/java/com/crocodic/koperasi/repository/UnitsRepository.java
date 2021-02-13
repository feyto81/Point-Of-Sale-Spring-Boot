package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Units;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitsRepository extends JpaRepository<Units, Long> {

    @Query(value = "select b from Units b where b.name like %:filter%")
    Page<Units> filterQuery(String filter, Pageable pageable);

}
