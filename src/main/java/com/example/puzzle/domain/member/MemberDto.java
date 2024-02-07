package com.example.puzzle.domain.member;

import com.example.puzzle.domain.model.constants.Role;
import com.example.puzzle.domain.model.entity.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
public class MemberDto implements UserDetails {
    private Long id;
    private String nickname;
    private List<String> roles;

    public static MemberDto from (Member member){
        List<String> roles = new ArrayList<>(member.getRoles());
        roles.add("ROLE_MEMBER");
        return new MemberDto(member.getId(), member.getNickname(), roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {

        return this.nickname;
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
