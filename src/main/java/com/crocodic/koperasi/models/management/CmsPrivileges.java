package com.crocodic.koperasi.models.management;

import javax.persistence.*;

@Entity
@Table(name = "cms_privileges")
public class CmsPrivileges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private int isSuperadmin;

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

    public int getIsSuperadmin() {
        return isSuperadmin;
    }

    public void setIsSuperadmin(int isSuperadmin) {
        this.isSuperadmin = isSuperadmin;
    }
}
