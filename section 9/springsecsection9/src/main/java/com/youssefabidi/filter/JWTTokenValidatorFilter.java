package com.youssefabidi.filter;

import com.youssefabidi.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the JWT token from the request header
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);

        // If a JWT token exists in the request header, try to validate it
        if (null != jwt) {
            try {
                // Parse and validate the JWT token using the secret key
                SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Extract the username and authorities from the validated token
                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");

                // Create an authentication object using the extracted username and authorities
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                // Set the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // If the token is invalid, throw a BadCredentialsException
                throw new BadCredentialsException("Invalid Token received!");
            }

        }

        // Pass the request and response to the next filter in the filter chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // This method determines whether the filter should be applied to the current request
        // In this case, the filter should not be applied to requests that match the "/user" endpoint
        return request.getServletPath().equals("/user");
    }

}

