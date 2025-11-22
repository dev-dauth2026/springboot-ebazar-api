package com.ebazar.ebazarapi.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ebazar.ebazarapi.entity.User;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // So we can still access full User later if needed
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // e.g. ROLE_USER, ROLE_ADMIN
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // hashed password
    }

    @Override
    public String getUsername() {
        // we treat email as username
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // you can add real flags later
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // add lock logic in future if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // same here
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // inactive users can’t log in
    }
}