package com.crocodic.koperasi.models;


import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cart_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = true)
    private Items items;


    @Column(name = "transaction_code", nullable = true)
    private String transaction_code;

    @Column(name = "price", nullable = true)
    private Integer price;

    @Column(name = "qty", nullable = true)
    private Integer qty;

    @Column(name = "discount_item", nullable = true)
    private String discount_item;

    @Column(name = "total", nullable = true)
    private String total;

    @Column(name = "user_id", nullable = true)
    private String user_id;

    @Column(name = "customer_name", nullable = true)
    private String customer_name;

    @Column(name = "created_at", nullable = true)
    private String created_at;

    public Cart() {

    }

    public Cart(String customer, Integer qty, String transaction_code, String id_users, Items item_idd, String created_at, String discount,Integer price,String total) {
        this.customer_name = customer;
        this.price = price;
        this.qty = qty;
        this.transaction_code = transaction_code;
        this.user_id = id_users;
        this.created_at = created_at;
        this.total = total;
        this.discount_item = discount;
        this.items = item_idd;
    }


    public Long getCart_id() {
        return cart_id;
    }

    public void setCart_id(Long cart_id) {
        this.cart_id = cart_id;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }



    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
