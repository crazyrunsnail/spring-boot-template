package com.github.crazyrunsnail.template.dto.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.crazyrunsnail.template.model.User;
import com.github.crazyrunsnail.template.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsDTO extends User implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StringUtils.isBlank(this.getRolesArrayJson())) {
            return Collections.emptyList();
        }
        return JsonUtils.parse(this.getRolesArrayJson(), new TypeReference<List<String>>() {})
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
