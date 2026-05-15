package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="tr_configuration")
@JsonIgnoreProperties(ignoreUnknown = true)
@Indexed
public class ConfigurationEntity extends TenantSpecificEntity {

    private static final long serialVersionUID = -6886848877574564547L;

    @Column(nullable = true, name = "name")
    private String name;
    @Column(nullable = true, name = "value")
    private String value;
    @Column(nullable = true, name = "category")
    private String category;
    @Column(nullable = true, name = "active")
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

