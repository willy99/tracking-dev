package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="tr_role")
@JsonIgnoreProperties(ignoreUnknown = true)
@Indexed
public class Role extends TenantSpecificEntity {

    private static final long serialVersionUID = -3467279920723998768L;

    @Column(unique = true, nullable = false, name="role_name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String roleName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Permission> permissionList;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "system_role")
    private boolean systemRole = false;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "assignable")
    private boolean assignable = true;

    @Transient
    private Integer users;

    @Transient
    private Integer checkedPermissions;

    @Type(type = "yes_no")
    private boolean editable = true;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String name) {
        this.roleName = name;
    }

    public Set<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(Set<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getCheckedPermissions() {
        return checkedPermissions;
    }

    public void setCheckedPermissions(Integer checkedPermissions) {
        this.checkedPermissions = checkedPermissions;
    }

    public boolean isSystemRole() {
        return systemRole;
    }

    public void setSystemRole(boolean systemRole) {
        this.systemRole = systemRole;
    }

    public boolean isAssignable() {
        return assignable;
    }

    public void setAssignable(boolean assignable) {
        this.assignable = assignable;
    }

    // ========================================================================
    /**
     * {@inheritDoc}
     * @see Object#hashCode()
     */
    public int hashCode(){
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getRoleName());
        return builder.toHashCode();
    }

    /**
     * {@inheritDoc}
     * @see Object#equals(Object)
     */
    public boolean equals(final Object object){
        if (!(object instanceof Role))
            return false;
        if (this == object)
            return true;
        final Role obj = (Role) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .append(getRoleName(), obj.getRoleName())
                .isEquals();
    }
}
