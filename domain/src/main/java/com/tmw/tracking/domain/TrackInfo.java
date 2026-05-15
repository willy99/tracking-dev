package com.tmw.tracking.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackInfo implements Serializable {

    private static final long serialVersionUID = -4648775306476835708L;
    private final static Logger logger = LoggerFactory.getLogger(TrackInfo.class);


    private Date date;
    private String location;
    private String vessel;

    public TrackInfo() {

    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getVessel() {
        return vessel;
    }

    public void setDate(String date) {
        if (date != null) {
            try {
                this.date = new SimpleDateFormat("yyyy-mm-dd").parse(date);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    @Override
    public String toString() {
        return "date" + date + "/"+ "(location"+ location + "vessel" + vessel + ")";
    }
}
