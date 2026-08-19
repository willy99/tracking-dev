package com.tmw.tracking.domain.events.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;
import com.tmw.tracking.entity.User;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event_log")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventLog extends TenantSpecificEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_user", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_date", nullable = false)
    private Date eventDate;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "permission")
    private String permission;

    @Column(name = "http_method")
    private String httpMethod;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "success")
    private boolean success = true;

    @Column(name = "error_message")
    private String errorMessage;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
