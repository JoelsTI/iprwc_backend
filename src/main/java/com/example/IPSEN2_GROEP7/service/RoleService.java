package com.example.IPSEN2_GROEP7.service;

import com.example.IPSEN2_GROEP7.Permission;
import com.example.IPSEN2_GROEP7.dao.RoleDAO;
import com.example.IPSEN2_GROEP7.model.Role;
import com.google.gson.Gson;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private List<Role> standardRoles;
    private final RoleDAO roleDAO;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createRoles() {
        if(!this.roleDAO.index().isEmpty()) {
            standardRoles = this.roleDAO.index();
            return;
        }

        ArrayList<Role> defaultRoles = new ArrayList<>();

        defaultRoles.add(new Role("Admin", "The guy that can break it all",
                "%s".formatted(
                        Permission.ADMIN)));
        defaultRoles.add(new Role("User", "The normal guy",
                "%s,%s,%s,%s,%s".formatted(
                        Permission.AUTHENTICATE, Permission.RESERVATION_READ, Permission.RESERVATION_WRITE,
                        Permission.RESERVATION_UPDATE, Permission.RESERVATION_DELETE)));
        defaultRoles.add(new Role("Disabled user", "Can't do anything",
                "%s".formatted(
                        Permission.NONE)));

        this.standardRoles = defaultRoles;
        for(Role role: standardRoles) {
            this.roleDAO.store(role);
        }
    }

    public List<Role> getRoles() {
        return standardRoles;
    }

    public List<Role> removeDoubleRoles(List<Role> roles) {
        for(int i = 0; i < roles.size(); i++) {
            for(int j = 0; j < roles.size(); j++) {
                if(roles.get(i).equals(roles.get(j)) && i != j) {
                    roles.remove(i);
                }
            }
        }
        return roles;
    }

    public boolean hasPermission(List<Role> roles, Permission permission) {
        boolean isAdmin = false;
        for(Role role: roles) {
            List<String> permissions = Arrays.asList(role.getPermissions());
            if(permissions.contains(String.valueOf(permission))) {
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    public boolean validatePermissions(List<Role> roles) {
        boolean isValid = true;
        List<String> allPermissions = List.of(Arrays.toString(Permission.values()));
        for(Role role: roles) {
            String[] permissions = role.getPermissions();
            for(String permission: permissions) {
                if(!allPermissions.contains(permission)) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
