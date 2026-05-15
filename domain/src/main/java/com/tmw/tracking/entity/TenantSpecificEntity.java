package com.tmw.tracking.entity;

import com.tmw.tracking.entity.support.TenantBridge;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import org.hibernate.search.annotations.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@FilterDefs({
        @FilterDef(name = "tenantSpecific",
                parameters = {@ParamDef(name = "tenant", type = "long")})
})
@MappedSuperclass
public abstract class TenantSpecificEntity extends UpdatableEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name="tenant", nullable = false)
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    @FieldBridge(impl = TenantBridge.class)
    private Company tenant;

    public Company getTenant() {
        return tenant;
    }

    public void setTenant(Company tenant) {
        this.tenant = tenant;
    }
}
