package com.tmw.tracking.domain.flex.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

/**
 * The API object for sync data for import stage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexImportPackage {

    public Set<Order> orders;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        public String orderNum;
        public String updatedDate;
        public Boolean deleted;
        public Set<Container> containers;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Container {
        public String containerNumber;
        public Integer flexQty;
        public String updatedDate;
        //public Set<Flex> flexs;
    }

    //for back<=>1c sync
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Flex {
        public String serialNum;
    }
}
