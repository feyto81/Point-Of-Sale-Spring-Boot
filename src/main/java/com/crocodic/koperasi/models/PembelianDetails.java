package com.crocodic.koperasi.models;


import javax.persistence.*;

@Entity
@Table(name = "pembelian_details")
public class PembelianDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pembelian_id")
    private String pembelian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pembelian_idd", nullable = true)
    private Pembelian pembelian1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = true)
    private Items items;

    @Column(name = "qty", nullable = true)
    private Integer qty;

    @Column(name = "created_at", nullable = true)
    private String createdAt;

    @Column(name = "subtotal", nullable = true)
    private Integer subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPembelian() {
        return pembelian;
    }

    public void setPembelian(String pembelian) {
        this.pembelian = pembelian;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Pembelian getPembelian1() {
        return pembelian1;
    }

    public void setPembelian1(Pembelian pembelian1) {
        this.pembelian1 = pembelian1;
    }
}
