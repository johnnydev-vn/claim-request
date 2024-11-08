package com.claimrequest.Auth;

import com.claimrequest.entities.Staff;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Setter
@Getter
public class CustomUserDetail implements UserDetails {

    final Staff staffDB;

    public CustomUserDetail(Staff staffDB) {
        this.staffDB = staffDB;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        int rankDB = staffDB.getRank();

        return List.of(new SimpleGrantedAuthority("ROLE_RANK_" + rankDB));
    }

    @Override
    public String getPassword() {
        return staffDB.getPassword();
    }

    @Override
    public String getUsername() {
        return staffDB.getUsername();
    }
}
