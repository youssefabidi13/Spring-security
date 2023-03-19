package com.youssefabidi.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {

    // Implementation of the convert method required by the Converter interface
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract the realm_access claim from the JWT
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        // If the realm_access claim is missing or empty, return an empty list of GrantedAuthority objects
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        // Otherwise, transform the list of role names into a collection of GrantedAuthority objects
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName) // Prefix each role name with "ROLE_" as required by Spring Security
                .map(SimpleGrantedAuthority::new) // Convert each role name into a new SimpleGrantedAuthority object
                .collect(Collectors.toList()); // Collect the resulting objects into a new collection

        return returnValue; // Return the collection of GrantedAuthority objects
    }

}
