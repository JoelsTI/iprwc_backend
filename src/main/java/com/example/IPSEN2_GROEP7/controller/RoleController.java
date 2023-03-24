package com.example.IPSEN2_GROEP7.controller;

import com.example.IPSEN2_GROEP7.dao.RoleDAO;
import com.example.IPSEN2_GROEP7.dto.RoleStoreDTO;
import com.example.IPSEN2_GROEP7.model.ApiResponse;
import com.example.IPSEN2_GROEP7.model.Role;
import com.example.IPSEN2_GROEP7.response.types.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/user/role")
public class RoleController {

    private final RoleDAO roleDAO;

    public RoleController(RoleDAO roleDAO){
        this.roleDAO = roleDAO;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity index(){
        ArrayList<Role> list = roleDAO.index();
        if(list.size() > 0){
           return new ApiResponse(HttpStatus.OK, list).getResponse();
        }

        return new ApiResponse(HttpStatus.NOT_FOUND, new Message("There are currently no roles")).getResponse();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity show(@PathVariable int id){
        if(isNull(roleDAO.show(id))){
            return new ApiResponse(HttpStatus.NOT_FOUND, "Role with id " + id + " could not be found").getResponse();
        }

        return new ApiResponse(HttpStatus.OK, roleDAO.show(id)).getResponse();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity store(@Valid @NotNull @RequestBody RoleStoreDTO roleStoreDTO){
        Role role = roleStoreDTO.toValidRole();
        boolean response = roleDAO.store(role);
        if(response){
            return new ApiResponse(HttpStatus.CREATED, role).getResponse();
        }

        return new ApiResponse(HttpStatus.BAD_REQUEST, new Message("Something went wrong with creating the role")).getResponse();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@Valid @NotNull @RequestBody RoleStoreDTO roleStoreDTO, @PathVariable int id){
        Role role = roleStoreDTO.toValidRole();
        boolean response = roleDAO.update(role, id);
        if(response){
            return new ApiResponse(HttpStatus.CREATED, role).getResponse();
        }

        return new ApiResponse(HttpStatus.BAD_REQUEST, new Message("The role you created is invalid")).getResponse();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable int id){
        boolean response = roleDAO.delete(id);
        if(response){
            return new ApiResponse(HttpStatus.NO_CONTENT, new Message("Deleted role with id " + id)).getResponse();
        }

        return new ApiResponse(HttpStatus.NOT_FOUND, new Message("Could not find role with id " + id)).getResponse();
    }
}
