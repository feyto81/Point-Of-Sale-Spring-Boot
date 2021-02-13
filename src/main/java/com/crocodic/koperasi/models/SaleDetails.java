package com.crocodic.koperasi.models;

import javax.persistence.*;

@Entity
@Table(name = "sale_details")
public class SaleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detail_id;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "sale_id", nullable = true)
//    private Sales sales;

    @Column(name = "sale_id", nullable = true)
    private String sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = true)
    private Items items;

    @Column(name = "price", nullable = true)
    private String price;

    @Column(name = "qty", nullable = true)
    private String qty;

    @Column(name = "discount_item", nullable = true)
    private String discount_item;

    @Column(name = "total", nullable = true)
    private String total;

    @Column(name = "customer_name", nullable = true)
    private String customer_name;

    @Column(name = "created_at", nullable = true)
    private String created_at;

    public SaleDetails() {
    }

    public SaleDetails(String sale_id, Items items, String price, String qty, String discount_item, String total, String customer_name, String created_at) {
        this.sale = sale_id;
        this.items = items;
        this.price = price;
        this.qty = qty;
        this.discount_item = discount_item;
        this.total = total;
        this.customer_name = customer_name;
        this.created_at = created_at;
    }

    public Long getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(Long detail_id) {
        this.detail_id = detail_id;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDiscount_item() {
        return discount_item;
    }

    public void setDiscount_item(String discount_item) {
        this.discount_item = discount_item;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
