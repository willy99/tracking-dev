package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.support.RoleBridge;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "tr_user")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@Indexed
public class User extends TenantSpecificEntity {

    private static final long serialVersionUID = 8872183864654190431L;
    @NotNull(message = "Unique ID cannot be null")
    @Column(nullable = false, unique = true)
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String email;
    @NotNull(message = "Unique Phone cannot be null")
    @Column(nullable = false, unique = true)
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String phone;
    @NotNull(message = "Password")
    @Column(nullable = false)
    private String password;
    @NotNull(message = "First Name cannot be null")
    @Column(nullable = false, name = "first_name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String firstName;
    @NotNull(message = "Last cannot be null")
    @Column(nullable = false, name = "last_name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String lastName;

    @Type(type = "yes_no")
    @Column(nullable = false, name = "active")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private boolean activity = true;

    @Column(name = "locale")
    private Locale locale;

    @ManyToOne
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    @FieldBridge(impl = RoleBridge.class)
    @JoinColumn(name="role", nullable = true)
    private Role role;

    @Column(nullable = true, name = "activation_hash")
    private String activationHash;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activation_created")
    private Date activationCreated;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return activity;
    }

    public void setActive(boolean activity) {
        this.activity = activity;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getActivationHash() {
        return activationHash;
    }

    public void setActivationHash(String activationHash) {
        this.activationHash = activationHash;
    }

    public Date getActivationCreated() {
        return activationCreated;
    }

    public void setActivationCreated(Date activationCreated) {
        this.activationCreated = activationCreated;
    }

    // ========================================================================

    /**
     * {@inheritDoc}
     *
     * @see Object#hashCode()
     */
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getEmail());
        return builder.toHashCode();
    }


    @Override
    public String toString() {
        return email;
    }

    /**
     * {@inheritDoc}
     *
     * @see Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (!(object instanceof User))
            return false;
        if (this == object)
            return true;
        final User obj = (User) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .append(getEmail(), obj.getEmail())
                .isEquals();
    }
}