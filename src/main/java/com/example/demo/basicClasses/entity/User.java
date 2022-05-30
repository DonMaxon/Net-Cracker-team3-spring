package com.example.demo.basicClasses.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "Users")
public class User implements UserDetails, CredentialsContainer {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public User() {
    }

    public User(UUID id) {
        this.id = id;
    }

    public User(UUID id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public User(UUID id, boolean isAdmin, String login, String password) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.login = login;
        this.password = password;
    }

    public User(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static UserDetails fromUser(User user){
        return new User(user.getId(), user.isAdmin(), user.getUsername(), user.getPassword());
    }

    @Override
    public void eraseCredentials() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "User{" +
                "id=" + id +
                ", isAdmin=" + isAdmin +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
