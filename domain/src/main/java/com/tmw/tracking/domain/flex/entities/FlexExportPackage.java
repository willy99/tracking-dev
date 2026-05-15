package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Set;

/**
 * The API object for sync data for import stage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexExportPackage {

    public Set<Order> orders;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        public String orderNum;
        public String updatedDate;
        public Date executionDate;
        public Integer exportContainerQty;
        public Boolean deleted;
        public Set<Container> containers; //for back<=>1c sync
    }

    //for back<=>1c sync
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Container {
        public Date updatedDate;
        public String containerNumber;
        public Integer flexQty;
    }

}

