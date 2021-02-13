package com.crocodic.koperasi.models;


import javax.persistence.*;

@Entity
@Table(name = "kas")
public class Kas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tanggal", nullable = true)
    private String tanggal;

    @Column(name = "faktur", nullable = true)
    private String faktur;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "jenis", nullable = true)
    private String jenis;

    @Column(name = "pemasukan", nullable = true)
    private Integer pemasukan;

    @Column(name = "pengeluaran", nullable = true)
    private Integer pengeluaran;

    @Column(name = "keterangan", nullable = true)
    private String keterangan;

    @Column(name = "created_at", nullable = true)
    private String createdAt;

    public Kas() {
    }

    public Kas(Long id, String tanggal, String faktur, String type, String jenis, Integer pemasukan, Integer pengeluaran, String keterangan, String createdAt) {
        this.id = id;
        this.tanggal = tanggal;
        this.faktur = faktur;
        this.type = type;
        this.jenis = jenis;
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
        this.keterangan = keterangan;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public Integer getPemasukan() {
        return pemasukan;
    }

    public void setPemasukan(Integer pemasukan) {
        this.pemasukan = pemasukan;
    }

    public Integer getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(Integer pengeluaran) {
        this.pengeluaran = pengeluaran;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
