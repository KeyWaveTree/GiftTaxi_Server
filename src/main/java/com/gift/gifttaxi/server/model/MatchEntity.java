package com.gift.gifttaxi.server.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "matched")
public class MatchEntity {
    @Id()
    @GeneratedValue()
    public Long id;
    public Long taxiId;
    public Long userId;
    public double startLatitude;
    public double startLongitude;
    public double endLatitude;
    public double endLongitude;
    public double estimateDistance;
    public int estimateCost;
    public int estimateTime;

    @Temporal(TemporalType.TIMESTAMP)
    public Date matchedAt;
}
