package com.crocodic.koperasi.models.management;

public class MenusRoles {

    private Long id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
