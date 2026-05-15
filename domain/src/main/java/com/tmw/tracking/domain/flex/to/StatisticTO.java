package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticTO {

    private int atBaseWarehouse = 0;
    private int atReserveWarehouse = 0;
    private int atWrittenWarehouse = 0;
    private int importFlexInProgress = 0;
    private int exported = 0;
    private int totalImported = 0;

    public void incrementBaseWarehouse() {
        atBaseWarehouse ++;
    }
    public void incrementReservedWarehouse() {
        atReserveWarehouse ++;
    }
    public void incrementWrittenWarehouse() {
        atWrittenWarehouse ++;
    }
    public void incremenTotal() {
        totalImported ++;
    }
    public void incrementExported() {exported ++; }

    public int getAtBaseWarehouse() {
        return atBaseWarehouse;
    }

    public void setAtBaseWarehouse(int atBaseWarehouse) {
        this.atBaseWarehouse = atBaseWarehouse;
    }

    public int getAtReserveWarehouse() {
        return atReserveWarehouse;
    }

    public void setAtReserveWarehouse(int atReserveWarehouse) {
        this.atReserveWarehouse = atReserveWarehouse;
    }

    public int getAtWrittenWarehouse() {
        return atWrittenWarehouse;
    }

    public void setAtWrittenWarehouse(int atWrittenWarehouse) {
        this.atWrittenWarehouse = atWrittenWarehouse;
    }

    public int getImportFlexInProgress() {
        return importFlexInProgress;
    }

    public void setImportFlexInProgress(int importFlexInProgress) {
        this.importFlexInProgress = importFlexInProgress;
    }

    public int getTotalImported() {
        return totalImported;
    }

    public void setTotalImported(int totalImported) {
        this.totalImported = totalImported;
    }

    public int getExported() {
        return exported;
    }

    public void setExported(int exported) {
        this.exported = exported;
    }
}
