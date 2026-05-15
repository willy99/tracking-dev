package com.tmw.tracking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackResult implements Serializable {

    private static final long serialVersionUID = -541071760666594548L;

    private String service;
    private List<TrackInfo> tracks;
    private String trackingNumber;

    public TrackResult() {

    }

    public TrackResult(String service, List<TrackInfo> tracks) {
        this.service = service;
        this.tracks = tracks;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setTracks(List<TrackInfo> tracks) {
        this.tracks = tracks;
    }

    public String getService() {
        return service;
    }

    public List<TrackInfo> getTracks() {
        return tracks;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Override
    public String toString() {
        return "service" + service;
    }
}
