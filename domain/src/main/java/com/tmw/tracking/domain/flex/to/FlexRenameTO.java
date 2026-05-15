package com.tmw.tracking.domain.flex.to;

import java.util.Date;

public class FlexRenameTO {
    String oldSerialNum;
    String newSerialNum;
    Date updatedDate;

    public String getOldSerialNum() {
        return oldSerialNum;
    }

    public void setOldSerialNum(String oldSerialNum) {
        this.oldSerialNum = oldSerialNum;
    }

    public String getNewSerialNum() {
        return newSerialNum;
    }

    public void setNewSerialNum(String newSerialNum) {
        this.newSerialNum = newSerialNum;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
