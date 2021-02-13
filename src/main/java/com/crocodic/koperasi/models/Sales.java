package com.crocodic.koperasi.models;

import com.crocodic.koperasi.models.management.CmsUsers;

import javax.persistence.*;

@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long sale_id;

    @Column(name = "invoice", nullable = true)
    private String invoice;

    @Column(name = "total_price", nullable = true)
    private String total_price;

    @Column(name = "discount", nullable = true)
    private String discount;

    @Column(name = "final_price", nullable = true)
    private Integer final_price;

    @Column(name = "cash", nullable = true)
    private String cash;

    @Column(name = "remaining", nullable = true)
    private String remaining;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "date", nullable = true)
    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private CmsUsers cmsUsers;

    @Column(name = "created_at", nullable = true)
    private String created_at;

    @Column(name = "customer", nullable = true)
    private String customer;

    @Column(name = "year", nullable = true)
    private String year;

    @Column(name = "month", nullable = true)
    private String month;

    public Sales() {

    }

    public Sales(String invoice,Long sale_id, String total_price, String discount, Integer final_price, String cash, String remaining, String note, String date, CmsUsers cmsUsers, String created_at) {
        this.invoice = invoice;
        this.sale_id = sale_id;
        this.total_price = total_price;
        this.discount = discount;
        this.final_price = final_price;
        this.cash = cash;
        this.remaining = remaining;
        this.note = note;
        this.date = date;
        this.cmsUsers = cmsUsers;
        this.created_at = created_at;
    }

    public Long getSale_id() {
        return sale_id;
    }

    public void setSale_id(Long sale_id) {
        this.sale_id = sale_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Integer getFinal_price() {
        return final_price;
    }

    public void setFinal_price(Integer final_price) {
        this.final_price = final_price;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CmsUsers getCmsUsers() {
        return cmsUsers;
    }

    public void setCmsUsers(CmsUsers cmsUsers) {
        this.cmsUsers = cmsUsers;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
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
