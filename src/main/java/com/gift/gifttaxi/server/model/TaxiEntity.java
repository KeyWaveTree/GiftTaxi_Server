package com.gift.gifttaxi.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Taxi")
public class TaxiEntity {
    @Id()
    @GeneratedValue()
    public Long id;
    public String taxiNumber;
    public String driver;
    public double latitude;
    public double longitude;
}
