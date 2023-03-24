package com.example.IPSEN2_GROEP7.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private EmployeeDetailsService employeeDetailsService;
    private JWTUtilization jwtUtil;

    public JWTFilter(EmployeeDetailsService employeeDetailsService, JWTUtilization jwtUtil) {
        this.employeeDetailsService = employeeDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){

            String jwt = authHeader.substring(7);
            if(jwt == null || jwt.isBlank()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The Bearer Header contains an invalid JWT Token, try entering a valid JWT Token");
            }
            else {
                try{
                    String email = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                    UserDetails employeeDetails = employeeDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, employeeDetails.getPassword(), employeeDetails.getAuthorities());
                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                catch(JWTVerificationException exc){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT Token is invalid, try entering a valid JWT token.");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}


