package com.crocodic.koperasi.repository.management;

import com.crocodic.koperasi.models.management.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query(value = "select b from Activity b where b.url like %:filter% or b.userAgent like %:filter% or b.ipAddress like %:filter% or b.description like %:filter%")
    Page<Activity> filterQuery(String filter, Pageable pageable);

}
