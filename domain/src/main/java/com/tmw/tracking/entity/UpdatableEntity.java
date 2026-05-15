package com.tmw.tracking.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pzhelnov on 5/9/2017.
 */
@MappedSuperclass
public abstract class UpdatableEntity extends RootEntity {

    private static final long serialVersionUID = 1L;

    @Version
    @Column(name = "version")
    private int version = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;

    protected static boolean getBooleanValue(final Boolean value) {
        return Boolean.valueOf(String.valueOf(value));
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdate) {
        this.lastUpdated = lastUpdate;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = new Date();
    }
}
