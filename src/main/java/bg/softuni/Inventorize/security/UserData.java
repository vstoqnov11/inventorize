package bg.softuni.Inventorize.security;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserData implements UserDetails {

    private UUID id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private Business business;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired () {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked () {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled () {
        return UserDetails.super.isEnabled();
    }
}
