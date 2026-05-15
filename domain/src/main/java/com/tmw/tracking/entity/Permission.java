package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.PermissionType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tr_permission")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission extends RootEntity {

    private static final long serialVersionUID = -3467279920723998768L;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, name="name")
    private PermissionType name;

    @ManyToOne
    @JoinColumn(name="parent", nullable = true)
    @JsonBackReference
    private Permission parentPermission;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "system_permission")
    private boolean systemPermission;

    @OneToMany(mappedBy="parentPermission")
    private List<Permission> children = new ArrayList<Permission>();

    public Permission getParentPermission() {
        return parentPermission;
    }

    public void setParentPermission(Permission parentPermission) {
        this.parentPermission = parentPermission;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    @Column(nullable = true, name = "description")
    private String description;

    public PermissionType getName() {
        return name;
    }

    public void setName(PermissionType name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(boolean systemPermission) {
        this.systemPermission = systemPermission;
    }

// ========================================================================
    /**
     * {@inheritDoc}
     * @see Object#hashCode()
     */
    public int hashCode(){
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getDescription());
        builder.append(getName());
        return builder.toHashCode();
    }
    /**
     * {@inheritDoc}
     * @see Object#equals(Object)
     */
    public boolean equals(final Object object){
        if (!(object instanceof Permission))
            return false;
        if (this == object)
            return true;
        final Permission obj = (Permission) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .append(getName(), obj.getName())
                .append(getDescription(), obj.getDescription())
                .isEquals();
    }

    @Override
    public String toString() {
        return "Permission {" +
                "id='" + getId() + '\'' +
                "description='" + description + '\'' +
                "children="+ children.size() +
                "' }'";
    }
}
