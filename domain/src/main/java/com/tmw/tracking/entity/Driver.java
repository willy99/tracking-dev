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
@Table(name="tr_driver")
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Driver extends TenantSpecificEntity {

    @NotNull(message = "First Name can't be null")
    @Column(nullable = false, name = "first_name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String firstName;

    @NotNull(message = "Last Name can't be null")
    @Column(nullable = false, name = "last_name")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String lastName;

    @Column(nullable = true, name = "trailer_number")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private String trailerNumber;

    @Column(nullable = true, name = "tractor_number")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private String tractorNumber;

    @Column(nullable = true, name = "mobile")
    private String mobile;


    @NotNull(message = "Mobile can't be null")
    @Column(nullable = false, name = "length")
    private Integer length;

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

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getTractorNumber() {
        return tractorNumber;
    }

    public void setTractorNumber(String tractorNumber) {
        this.tractorNumber = tractorNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
