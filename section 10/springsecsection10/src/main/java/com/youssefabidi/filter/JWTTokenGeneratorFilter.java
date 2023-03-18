package com.youssefabidi.filter;

import com.youssefabidi.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**

 This class represents a filter that generates JWT tokens for authentication and authorization purposes in a web application.

 It extends the "OncePerRequestFilter" class to ensure that the filter is only applied once per request.
 */
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    /**

     This method generates a JWT token and sets it in the HTTP response header if the user is authenticated.

     It extracts the authentication information from the "SecurityContextHolder" and creates a JWT token using the "Jwts" class.

     The token contains the username and authorities (roles) of the authenticated user.

     The expiration time of the token is set to 30,000 seconds (or 30000/60 = 500 minutes).

     @param request the HttpServletRequest object

     @param response the HttpServletResponse object

     @param filterChain the FilterChain object

     @throws ServletException if an error occurs while processing the request

     @throws IOException if an error occurs while sending the response
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// If the user is authenticated, generate a JWT token
        if (null != authentication) {
// Generate a secret key for the token using the JWT_KEY defined in the SecurityConstants class
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
// Build the JWT token using the Jwts builder and set its claims
            String jwt = Jwts.builder().setIssuer("Abidi Bank").setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000000))//en milliseconde
                    .signWith(key).compact();
// Set the JWT token in the HTTP response header
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
        }

        filterChain.doFilter(request, response);
    }

    /**

     This method checks whether the filter should be applied to the request or not.
     The filter is only applied to the "/user" path.
     @param request the HttpServletRequest object
     @return true if the filter should not be applied to the request, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/user");
    }
    /**

     This method converts the collection of GrantedAuthority objects into a comma-separated string of authorities.
     @param collection the collection of GrantedAuthority objects
     @return a comma-separated string of authorities
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
// Iterate through the collection of authorities and add their names to the authoritiesSet
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
// Convert the set of authorities into a comma-separated string
        return String.join(",", authoritiesSet);
    }
}