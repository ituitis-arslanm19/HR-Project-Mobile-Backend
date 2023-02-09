//package com.hrProject.HR.Project.security.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hrProject.HR.Project.model.User;
//import com.hrProject.HR.Project.security.JwtUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//@AllArgsConstructor
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private AuthenticationManager authenticationManager;
//
//    private JwtUtils jwtUtils;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        try {
//            User credentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
//            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),null ,new ArrayList<>()));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//        String token = jwtUtils.generateJwtToken(authResult);
//
//        String body = ((User) authResult.getPrincipal()).getUsername() + " " + token;
//        response.getWriter().write(body);
//        response.getWriter().flush();
//    }
//}
