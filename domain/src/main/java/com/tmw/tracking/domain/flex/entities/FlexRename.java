package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.TenantSpecificEntity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="flex_rename")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class FlexRename extends TenantSpecificEntity {
    @NotNull(message = "SerialNumber can't be null")
    @Column(nullable = false, name = "old_serial_number")
    private String oldSerialNumber;

    @NotNull(message = "SerialNumber can't be null")
    @Column(nullable = false, name = "new_serial_number")
    private String newSerialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="related_flex", nullable=false, updatable = false)
    @IndexedEmbedded
    private Flex relatedFlex;

    public String getOldSerialNumber() {
        return oldSerialNumber;
    }

    public void setOldSerialNumber(String oldSerialNumber) {
        this.oldSerialNumber = oldSerialNumber;
    }

    public String getNewSerialNumber() {
        return newSerialNumber;
    }

    public void setNewSerialNumber(String newSerialNumber) {
        this.newSerialNumber = newSerialNumber;
    }

    public Flex getRelatedFlex() {
        return relatedFlex;
    }

    public void setRelatedFlex(Flex relatedFlex) {
        this.relatedFlex = relatedFlex;
    }
}
