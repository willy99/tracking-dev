package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.support.UserInfo;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tr_company")
@JsonIgnoreProperties(ignoreUnknown = true)
@Indexed
public class Company extends UpdatableEntity {

    @Column(nullable = false, name = "name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String name;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "active")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private boolean active = true;

    @Transient
    private Integer orders;

    @Transient
    private Integer activeUsers;

    @Transient
    private Integer inactiveUsers;

    @Transient
    private UserInfo admin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Integer getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Integer getInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(Integer inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    public UserInfo getAdmin() {
        return admin;
    }

    public void setAdmin(UserInfo admin) {
        this.admin = admin;
    }

}