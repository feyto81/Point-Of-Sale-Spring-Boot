package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Kas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KasRepository extends JpaRepository<Kas, Long> {

    @Query(value = "select b from Kas b where b.faktur like %:filter% or b.jenis like %:filter% or b.keterangan like %:filter% or b.type like %:filter%")
    Page<Kas> filterQuery(String filter, Pageable pageable);

    @Query(value = "select b from Kas b where b.tanggal BETWEEN :startDate AND :endDate")
    List<Kas> findAllByTanggal(String startDate, String endDate);

    @Query(value = "select SUM(b.pemasukan) from Kas b where b.tanggal BETWEEN :startDate AND :endDate")
    String selectTotalPendapatanByDateBeetween(String startDate, String endDate);

    @Query(value = "select SUM(b.pengeluaran) from Kas b where b.tanggal BETWEEN :startDate AND :endDate")
    String selectTotalPengeluaranByDateBeetween(String startDate, String endDate);

    @Query(value = "select SUM(b.pemasukan) from Kas b")
    String selectTotalPendapatan();

    @Query(value = "select SUM(b.pengeluaran) from Kas b")
    String selectTotalPengeluaran();
}
