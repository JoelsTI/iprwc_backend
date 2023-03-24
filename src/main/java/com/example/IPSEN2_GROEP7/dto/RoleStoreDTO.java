package com.example.IPSEN2_GROEP7.dto;

import com.example.IPSEN2_GROEP7.model.Role;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RoleStoreDTO {
    @Pattern(regexp = "^[A-Za-z]+$")
    @NotNull(message = "A title is required")
    private String title;

    @NotNull(message = "A description is required")
    String description;

    @NotNull(message = "A permissions list is required")
    String [] permissions;

    public Role toValidRole(){
        Role role = new Role();
        role.setTitle(this.title);
        role.setDescription(this.description);
        role.setPermissions(this.permissions);
        return role;
    }
}
