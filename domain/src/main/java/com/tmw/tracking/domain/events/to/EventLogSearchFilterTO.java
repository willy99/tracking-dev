package com.tmw.tracking.domain.events.to;

import java.util.Date;

public class EventLogSearchFilterTO {

    private String userEmail;
    private String action;
    private Boolean successOnly;
    private Date dateFrom;
    private Date dateTo;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getSuccessOnly() {
        return successOnly;
    }

    public void setSuccessOnly(Boolean successOnly) {
        this.successOnly = successOnly;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
