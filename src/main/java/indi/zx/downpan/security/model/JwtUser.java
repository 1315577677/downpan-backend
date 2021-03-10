package indi.zx.downpan.security.model;

import indi.zx.downpan.entity.UserEntity;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
@Getter
public class JwtUser implements UserDetails {

    private String id;
    private String username;
    private String password;
    private Long capacity;
    private Long used;
    private String imgUrl;
    private String name;


    public JwtUser() {
    }

    // 写一个能直接使用user创建jwtUser的构造器
    public JwtUser(UserEntity user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        capacity = user.getDiskCapacity();
        name = user.getName();
        imgUrl = user.getImgUrl();
        used = 0L;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "JwtUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
