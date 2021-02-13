package com.crocodic.koperasi.models;

import com.crocodic.koperasi.models.management.CmsUsers;

import javax.persistence.*;

@Entity
@Table(name = "pembelian")
public class Pembelian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "faktur", nullable = true)
    private String faktur;

    @Column(name = "tanggal_pembelian")
    private String tanggalPembelian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = true)
    private Suppliers suppliers;

    @Column(name = "total",nullable = true)
    private Integer total;

    @Column(name = "created_at", nullable = true)
    private String createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private CmsUsers cmsUsers;

    @Column(name = "year", nullable = true)
    private String year;

    @Column(name = "month", nullable = true)
    private String month;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getTanggalPembelian() {
        return tanggalPembelian;
    }

    public void setTanggalPembelian(String tanggalPembelian) {
        this.tanggalPembelian = tanggalPembelian;
    }

    public Suppliers getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Suppliers suppliers) {
        this.suppliers = suppliers;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CmsUsers getCmsUsers() {
        return cmsUsers;
    }

    public void setCmsUsers(CmsUsers cmsUsers) {
        this.cmsUsers = cmsUsers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
