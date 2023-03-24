package com.example.IPSEN2_GROEP7.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String permissions;

    public Role() {}

    public Role(String title, String description, String permissions) {
        this.title = title;
        this.description = description;
        this.permissions = permissions;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String [] getPermissions() {
        return permissions.split(",");
    }

    public void setPermissions(String [] permissions) {
        this.permissions = String.join(",", permissions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id && Objects.equals(title, role.title) && Objects.equals(description, role.description) && Objects.equals(permissions, role.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, permissions);
    }
}
