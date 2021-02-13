package com.crocodic.koperasi.models;


import com.crocodic.koperasi.models.management.CmsUsers;

import javax.persistence.*;

@Entity
@Table(name = "cart_pembelian")
public class CartPembelian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "faktur")
    private String faktur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = true)
    private Items items;

    @Column(name = "price", nullable = true)
    private Integer price;

    @Column(name = "qty", nullable = true)
    private Integer qty;

    @Column(name = "created_at", nullable = true)
    private String createdAt;

    @Column(name = "subtotal", nullable = true)
    private Integer subtotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private CmsUsers cmsUsers;

    @Column(name = "item_id1", nullable = true)
    private String item;

    public CartPembelian() {

    }

    public CartPembelian(String faktur, Integer qty, CmsUsers id_users, Items item_idd, String created_at, Integer price, Integer sub_total, String item_id1) {
        this.qty = qty;
        this.cmsUsers = id_users;
        this.items = item_idd;
        this.createdAt = created_at;
        this.price = price;
        this.subtotal = sub_total;
        this.item = item_id1;
        this.faktur = faktur;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CmsUsers getCmsUsers() {
        return cmsUsers;
    }

    public void setCmsUsers(CmsUsers cmsUsers) {
        this.cmsUsers = cmsUsers;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }
}
