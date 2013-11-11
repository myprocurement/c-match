package com.avricot.cboost.security;

import com.avricot.cboost.domain.user.Authority;
import com.avricot.cboost.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link org.springframework.security.core.userdetails.UserDetails} of spring-security that contains a
 * {@link User}.
 */
public final class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final User user;

    private final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

    /**
     * Unique constructor of {@link UserDetailsImpl} that takes a {@link User}
     * in order to construct the list of {@link org.springframework.security.core.GrantedAuthority}.
     */
    public UserDetailsImpl(final User user) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(grantedAuthorities, "grantedAuthorities must not be null");
        this.user = user;
        for(Authority authority : user.getAuthorities()){
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 31 * ObjectUtils.nullSafeHashCode(getUser());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()).append("[username:").append(getUsername()).append("]").toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.core.userdetails.UserDetails#getAuthorities
     * ()
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * Returns the current {@link User}.
     */
    public User getUser() {
        return user;
    }

}