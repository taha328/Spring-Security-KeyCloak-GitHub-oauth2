package com.example.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtConverterProperties properties;

    public JwtConverter(JwtConverterProperties properties) {
        this.properties = properties;
    }

    private String extractEmail(Jwt jwt) {

        return jwt.getClaim("email");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String email = extractEmail(jwt);
        if (email == null || !email.contains("@etu.uae.ac.ma")) {
            throw new IllegalArgumentException("Invalid email domain. Only @etu.uae.ac.ma addresses are allowed.");
        }
        Collection<GrantedAuthority> authorities = Stream.concat(
                Stream.concat(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractRealmRoles(jwt).stream()
                ),
                Stream.empty() // Closing the stream correctly
        ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }


    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Collection<String> realmRoles;

        if (realmAccess == null
                || (realmRoles = (Collection<String>) realmAccess.get("roles")) == null) {
            return Set.of();
        }
        return realmRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}


