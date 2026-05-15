package com.tmw.tracking.entity.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;

import java.util.Locale;

/**
 * Immutable object to keep user data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {

    private Long id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String password;
    private String oldPassword;
    private boolean active = true;
    private Locale locale;
    private String role;
    private int version;

    public UserInfo() {}

    public UserInfo(User user) {
        if (user != null) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.password = user.getPassword();
            this.oldPassword = user.getPassword();
            this.active = user.isActive();
            this.locale = user.getLocale();
            this.role = user.getRole()!=null?user.getRole().getRoleName():null;
            this.version = user.getVersion();
        }
    }

    private String convertPhone(String phone) {
        return phone!=null?phone.replaceAll("[()\\- ]", ""):null;
    }

    /**
     * will return flat user object
     */
    public User convert(RoleDao roleDao) {
        User user = new User();
        user.setId(this.getId());
        user.setEmail(this.getEmail());
        user.setPhone(convertPhone(this.getPhone()));
        user.setFirstName(this.getFirstName());
        user.setLocale(this.getLocale());
        user.setLastName(this.getLastName());
        user.setPassword(this.getPassword());
        user.setVersion(this.getVersion());
        if (this.getRole()!=null) {
            Role role = roleDao.getByRoleName(this.getRole());
            user.setRole(role);
        }
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public int getVersion() {
        return version;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
