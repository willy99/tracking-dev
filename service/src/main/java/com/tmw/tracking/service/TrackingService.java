package com.tmw.tracking.service;

public interface TrackingService {

    String trackContainer(String containerNumber);

    String trackAuction(String lot, String vin, String auction);
}
