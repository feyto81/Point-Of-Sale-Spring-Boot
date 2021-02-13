package com.crocodic.koperasi.repository;

import com.crocodic.koperasi.models.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    Sales findByInvoice(String invoice);

    @Query(value = "select b from Sales b where b.invoice like %:filter% or b.date like %:filter% or b.customer like %:filter% or b.total_price like %:filter% or b.cash like %:filter%")
    Page<Sales> filterQuery(String filter, Pageable pageable);

    //    @Query(value = "SELECT SUM(final_price) FROM Sales", nativeQuery = true)
    @Query("SELECT SUM(m.final_price) FROM Sales m  WHERE m.date LIKE %?1%")
    String selectTotal(String date);

    @Query("SELECT SUM(m.final_price) FROM Sales m")
    String selectTotal2();

    @Query(value = "SELECT * FROM sales",nativeQuery = true)
    public List<Sales> findAll();

    @Query("SELECT SUM(e.final_price) FROM Sales e WHERE e.year LIKE %?1%")
    String selectTotalByYear(String year);

    @Query("SELECT SUM(e.final_price) FROM Sales e WHERE e.month LIKE %?1%")
    String selectTotalByMonth(String month);

//    @Query("select e from Sales e where year(e.year) = ?1 and month(e.month) = ?2")
//    List<Sales> getSalesByMonthAndYear(String month, String year);

    @Query("SELECT SUM(m.final_price) FROM Sales m")
    String selectTotal3();

    @Query(value = "select b from Sales b where b.date BETWEEN :startDate AND :endDate")
    List<Sales> findAllByTanggal(String startDate, String endDate);

    @Query(value = "select SUM(b.final_price) from Sales b where b.date BETWEEN :startDate AND :endDate")
    String selectTotalByDateBeetween(String startDate, String endDate);
}
