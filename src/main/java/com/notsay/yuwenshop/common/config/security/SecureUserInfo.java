package com.notsay.yuwenshop.common.config.security;

import com.notsay.yuwenshop.common.enums.UserRoleEnum;
import com.notsay.yuwenshop.domain.entity.main.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/15 17:06
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecureUserInfo implements UserDetails {
    private Integer userId;

    private String username;

    private Integer role;

    private String mobile;

    private List<GrantedAuthority> authorities;

    public static SecureUserInfo genFromUserEntity(UserEntity userEntity) {
        return SecureUserInfo.builder()
                .userId(userEntity.getId())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .mobile(userEntity.getMobile())
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(UserRoleEnum.getRoleByCode(role).name());
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId);
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
        return true;
    }
}