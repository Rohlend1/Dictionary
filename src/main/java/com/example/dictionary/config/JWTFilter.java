package com.example.dictionary.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.dictionary.security.JwtUtil;
import com.example.dictionary.services.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PersonDetailsService detailsService;

    @Autowired
    public JWTFilter(JwtUtil jwtUtil, PersonDetailsService detailsService) {
        this.jwtUtil = jwtUtil;
        this.detailsService = detailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isBlank()){
            if(!authHeader.startsWith("Bearer ")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid header");
                return;
            }
            String jwt = authHeader.substring(7);

            if(jwt.isBlank()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT token in Bearer header");
                return;
            }
            else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails personDetails = detailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(personDetails,personDetails.getPassword(), personDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                catch (JWTVerificationException e){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT token");
                    return;
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
