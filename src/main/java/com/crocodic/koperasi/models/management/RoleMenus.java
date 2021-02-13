package com.crocodic.koperasi.models.management;

import java.util.List;

public class RoleMenus {

    private Long id;
    private String name;
    private String icon;
    private String path;
    private Long parent_id;
    private int sorting;
    private List<RoleMenus> roleMenus;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public List<RoleMenus> getRoleMenus() {
        return roleMenus;
    }

    public void setRoleMenus(List<RoleMenus> roleMenus) {
        this.roleMenus = roleMenus;
    }
}
