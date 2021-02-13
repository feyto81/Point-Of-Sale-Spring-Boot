package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Pembelian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PembelianRepository extends JpaRepository<Pembelian, Long> {

    @Query(value = "select b from Pembelian b where b.tanggalPembelian like %:filter% or b.faktur like %:filter% or b.suppliers.name like %:filter%")
    Page<Pembelian> filterQuery(String filter, Pageable pageable);

    @Query("SELECT SUM(m.total) FROM Pembelian m  WHERE m.tanggalPembelian LIKE %?1%")
    String selectTotal(String date);

    @Query("SELECT SUM(m.total) FROM Pembelian m")
    String selectTotal2();


    @Query("SELECT SUM(e.total) FROM Pembelian e WHERE e.year LIKE %?1%")
    String selectTotalByYear(String year);

    @Query("SELECT SUM(e.total) FROM Pembelian e WHERE e.month LIKE %?1%")
    String selectTotalByMonth(String month);

//    @Query("SELECT p.faktur ,d.name FROM Pembelian p JOIN p.suppliers d")
//    List<Pembelian> get();
//    @Query(value = "SELECT e.faktur FROM Pembelian e INNER JOIN PembelianDetails d ON d.pembelian1 = 3")
//    List<Pembelian> findAll();

    @Query("SELECT SUM(m.total) FROM Pembelian m")
    String selectTotal3();

    @Query(value = "select b from Pembelian b where b.tanggalPembelian BETWEEN :startDate AND :endDate")
    List<Pembelian> findAllByTanggal(String startDate, String endDate);

    @Query(value = "select SUM(b.total) from Pembelian b where b.tanggalPembelian BETWEEN :startDate AND :endDate")
    String selectTotalByDateBeetween(String startDate, String endDate);
}
