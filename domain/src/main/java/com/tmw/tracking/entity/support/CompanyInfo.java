package com.tmw.tracking.entity.support;

public class CompanyInfo {

    private Long id;
    private String name;
    private Integer activeUsers;
    private Integer disabledUsers;
    private Integer orders;

    public CompanyInfo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Integer getDisabledUsers() {
        return disabledUsers;
    }

    public void setDisabledUsers(Integer disabledUsers) {
        this.disabledUsers = disabledUsers;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }
}
