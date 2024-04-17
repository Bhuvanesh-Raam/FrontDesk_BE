package com.example.FrontDesk_BE.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt)
    {
        List<String> roles=jwt.getClaimAsStringList("roles");
        System.out.println("User roles from token: " + roles);
        return roles.stream()
                .map(role-> new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toList());
    }
}
