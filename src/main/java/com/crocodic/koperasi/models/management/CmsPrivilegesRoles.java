package com.crocodic.koperasi.models.management;


import javax.persistence.*;

@Entity
@Table(name = "cms_privileges_roles")
public class CmsPrivilegesRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cms_privileges", nullable = false)
    private CmsPrivileges cmsPrivileges;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cms_menus", nullable = false)
    private CmsMenus cmsMenus;
    private int can_view;
    private int can_add;
    private int can_edit;
    private int can_delete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CmsPrivileges getCmsPrivileges() {
        return cmsPrivileges;
    }

    public void setCmsPrivileges(CmsPrivileges cmsPrivileges) {
        this.cmsPrivileges = cmsPrivileges;
    }

    public CmsMenus getCmsMenus() {
        return cmsMenus;
    }

    public void setCmsMenus(CmsMenus cmsMenus) {
        this.cmsMenus = cmsMenus;
    }

    public int getCan_view() {
        return can_view;
    }

    public void setCan_view(int can_view) {
        this.can_view = can_view;
    }

    public int getCan_add() {
        return can_add;
    }

    public void setCan_add(int can_add) {
        this.can_add = can_add;
    }

    public int getCan_edit() {
        return can_edit;
    }

    public void setCan_edit(int can_edit) {
        this.can_edit = can_edit;
    }

    public int getCan_delete() {
        return can_delete;
    }

    public void setCan_delete(int can_delete) {
        this.can_delete = can_delete;
    }
}
