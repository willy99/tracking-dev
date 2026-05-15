package com.tmw.tracking.domain;

import com.tmw.tracking.entity.AuthenticatedUser;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class LoginResponse {
    private String token;
    private Date expirationDate;
    private String role;
    private List<String> permissions;


    public LoginResponse(AuthenticatedUser authenticatedUser) {
        this.token = authenticatedUser.getToken();
        this.expirationDate = authenticatedUser.getExpired();
    }

    public LoginResponse(String token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
