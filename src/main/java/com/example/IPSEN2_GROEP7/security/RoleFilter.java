package com.example.IPSEN2_GROEP7.security;

import com.example.IPSEN2_GROEP7.Permission;
import com.example.IPSEN2_GROEP7.dao.EmployeeDAO;
import com.example.IPSEN2_GROEP7.model.Employee;
import com.example.IPSEN2_GROEP7.model.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class RoleFilter extends OncePerRequestFilter {

    private final EmployeeDAO employeeDAO;

    private final LinkedHashMap<String, String[]> routePermissions;

    public RoleFilter(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
        this.routePermissions = new LinkedHashMap<String, String[]>();

        this.routePermissions.put("GET /api/user",
                new String[] {
                        String.valueOf(Permission.AUTHENTICATE)});
        this.routePermissions.put("GET /api/user/info",
                new String[]{
                        String.valueOf(Permission.AUTHENTICATE)});
        this.routePermissions.put("PUT /api/user/*",
                new String[]{
                        String.valueOf(Permission.ADMIN)});
        this.routePermissions.put("DELETE /api/user/*",
                new String[]{
                        String.valueOf(Permission.ADMIN)});
        this.routePermissions.put("GET /api/user/role",
                new String[]{
                        String.valueOf(Permission.ADMIN)});
        this.routePermissions.put("GET /api/user/role/*",
                new String[]{
                        String.valueOf(Permission.ADMIN)});
        this.routePermissions.put("POST /api/user/role",
                new String[]{
                        String.valueOf(Permission.ILLEGAL)});
        this.routePermissions.put("PUT /api/user/role/*",
                new String[]{
                        String.valueOf(Permission.ILLEGAL)});
        this.routePermissions.put("DELETE /api/user/role/*",
                new String[]{
                        String.valueOf(Permission.ILLEGAL)});
        this.routePermissions.put("POST /api/products",
                new String[]{
                        String.valueOf(Permission.ADMIN)});
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String key = request.getMethod() + " " + request.getRequestURI();
        List<Integer> pathVariables = new ArrayList<>();
        boolean access = false;

        for(String value: key.split("/")) {
            try{
                pathVariables.add(Integer.parseInt(value));
            } catch (NumberFormatException e) {
            }
        }

        key = key.replaceAll("\\d+","*");

        if(routePermissions.containsKey(key)) {

            // Getting all the user permissions
            Employee employee = employeeDAO.getEmployeeDetails();
            String[] needed = routePermissions.get(key);
            List<String> userPermissions = new ArrayList<>();

            for(Role role: employee.getRoles()) {
                userPermissions.addAll(Arrays.asList(role.getPermissions()));
            }

            String method = request.getMethod();
            if((Objects.equals(method, "DELETE") || Objects.equals(method, "PUT"))
                    && !Arrays.asList(needed).contains(String.valueOf(Permission.ADMIN))) {

            } else {
                access = true;
            }

            // Check if access has changed to false after the switch statement
            if(!access) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        String.format("User is unauthorized to use %s on endpoint %s&n", request.getMethod(), request.getRequestURI())
                );
            }

            // Check if the user has the permissions
            for(String permission: needed) {
                if(userPermissions.contains(permission)) {
                    access = true;
                } else {
                    access = false;
                    break;
                }
            }

            // Send 403 if user doesn't have all the required permissions
            if(!access) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        String.format("User is unauthorized to use %s on endpoint %s&n", request.getMethod(), request.getRequestURI())
                );
            }
        }
        filterChain.doFilter(request, response);
    }
}
