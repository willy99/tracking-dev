package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="tr_line")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class TrackingLine extends RootEntity {
    private static final long serialVersionUID = -6886848877574564547L;

    @NotNull(message = "Name can't be null")
    @Column(nullable = false, name = "name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
