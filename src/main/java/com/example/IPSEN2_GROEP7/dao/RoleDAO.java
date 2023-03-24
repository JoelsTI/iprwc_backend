package com.example.IPSEN2_GROEP7.dao;

import com.example.IPSEN2_GROEP7.model.Role;
import com.example.IPSEN2_GROEP7.repositories.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class RoleDAO {
    private final RoleRepository roleRepository;

    public RoleDAO(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    /**
     * Returns an arraylist of all different roles.
     * @return Arraylist of role objects
     */
    public ArrayList<Role> index(){
        return (ArrayList<Role>) roleRepository.findAll();
    }

    /**
     * Returns a role object with the given id.
     * If the role can't be found, null is returned.
     * @param id
     * @return role
     */
    public Role show(int id){
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    /**
     * Insert a new role into the role table and returns 1 if it was successful.
     * If the role object is null or there already exists a role with the id of the passed role object, it returns 0.
     * @param role
     * @return integer
     */
    public boolean store(Role role){
        if(role != null && roleRepository.findById(role.getId()).isEmpty()){
            roleRepository.save(role);
            return true;
        } else{
            return false;
        }
    }

    /**
     * Updates a role object and returns 1 if it was successful.
     * This method calls a method from the RoleRepository.
     * If the update statement failed, 0 is returned.
     * @param role
     * @return integer
     */

    public boolean update(Role role, int id){
        String permissions = String.join(",", role.getPermissions());
        try{
            roleRepository.update(permissions, role.getDescription(), role.getTitle(), id);
            return true;
        } catch (Exception e){
            return false;
        }

    }

    /**
     * Deletes a role and returns 1 if the deletion was successful.
     * Returns 0 if the role with the specified id was not found.
     * @param id
     * @return integer
     */
    public boolean delete(int id){
        if(roleRepository.findById(id).isPresent()){
            roleRepository.deleteById(id);
            return true;
        } else{
            return false;
        }
    }

}
