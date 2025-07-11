package com.powernode.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class SecurityUser implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Long shopId;

    private String loginType;
    private Set<String> perms=new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginType+this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1 ;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }

    public Set<String> getPerms() {
        HashSet<String> finalPermsSet = new HashSet<>();
        perms.forEach( perm -> {
            if (perm.contains(",")) {
                String[] realPerms = perm.split(",");

                for (String realPerm : realPerms) {
                    finalPermsSet.add(realPerm);
                }
            }else{
                    finalPermsSet.add(perm);
                }
            } );
           return finalPermsSet;
        }
    }