package com.crocodic.koperasi.models.management;


import javax.persistence.*;

@Entity
@Table(name = "cms_users")
public class CmsUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "foto",nullable = true)
    private String foto;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cms_privileges", nullable = false)
    private CmsPrivileges cmsPrivileges;

    @Column(name = "status",nullable = true)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CmsPrivileges getCmsPrivileges() {
        return cmsPrivileges;
    }

    public void setCmsPrivileges(CmsPrivileges cmsPrivileges) {
        this.cmsPrivileges = cmsPrivileges;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
