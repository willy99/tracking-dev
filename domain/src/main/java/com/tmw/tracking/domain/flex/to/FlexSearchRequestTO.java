package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request body for the "get flexes by order" endpoint.
 * Carries the order identity (orderNum + orderType) plus all paging/filter params.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexSearchRequestTO {

    private String orderNum;
    private String orderType;   // IMPORT | EXPORT | MOUNT
    private SearchFilterTO filter;

    public String getOrderNum() { return orderNum; }
    public void setOrderNum(String orderNum) { this.orderNum = orderNum; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public SearchFilterTO getFilter() { return filter; }
    public void setFilter(SearchFilterTO filter) { this.filter = filter; }
}
