package com.gift.gifttaxi.server.service;

import com.gift.gifttaxi.server.model.MatchEntity;
import com.gift.gifttaxi.server.model.TaxiEntity;
import com.gift.gifttaxi.server.repository.TaxiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaxiService {
    private final TaxiRepository taxiRepository;
    private final MatchService matchService;
    private static final int KM_RADIUS = 6371;
    private static final double MAX_SPEED_PER_MIN = 1;
    private static final int DEFAULT_COST = 3800;
    private static final int EXTRA_COST = 100;
    private static final int DEFAULT_COST_DISTANCE = 2;

    @Autowired
    public TaxiService(TaxiRepository taxiRepository,
                       MatchService matchService) {
        this.taxiRepository = taxiRepository;
        this.matchService = matchService;
    }

    public void addTaxiPosition(TaxiEntity taxi) {
        this.taxiRepository.save(taxi);
    }

    public ArrayList<TaxiEntity> findTaxiList() {
        ArrayList<TaxiEntity> taxiList = new ArrayList(this.taxiRepository.findAll());
        return taxiList;
    }

    private double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }

    public double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double distanceLatitude = degreeToRadian(latitude1 - latitude2);
        double distanceLongitude = degreeToRadian(longitude1 - longitude2);
        double value = Math.pow(Math.sin(distanceLatitude / 2), 2) +
                Math.pow(Math.sin(distanceLongitude / 2), 2) *
                        Math.cos(degreeToRadian(latitude1)) * Math.cos(degreeToRadian(latitude2));
        double c = 2 * Math.atan2(Math.sqrt(value), Math.sqrt(1 - value));
        return KM_RADIUS * c;
    }

    public int calculateEstimateTime(double latitude1, double longitude1, double latitude2, double longitude2) {
        double distance = this.calculateDistance(latitude1, longitude1, latitude2, longitude2);
        double movePerMinute = distance / this.MAX_SPEED_PER_MIN;
        return (int) Math.ceil(movePerMinute);
    }

    public int calculateEstimateCost(double latitude1, double longitude1, double latitude2, double longitude2) {
        double distance = this.calculateDistance(latitude1, longitude1, latitude2, longitude2);
        if (distance < DEFAULT_COST_DISTANCE) {
            return DEFAULT_COST;
        }
        int extraDistance = (int) Math.ceil((distance - DEFAULT_COST_DISTANCE) / 0.1);
        return DEFAULT_COST + extraDistance * EXTRA_COST;
    }

    public TaxiEntity match(double startLatitude,
                            double startLongitude,
                            double endLatitude,
                            double endLongitude,
                            long userId,
                            int limit) {
        ArrayList<TaxiEntity> taxis = new ArrayList(this.taxiRepository.findAll());
        List<TaxiEntity> matchList = taxis.stream().
                filter(taxiEntity -> this.calculateEstimateTime(
                        taxiEntity.latitude, taxiEntity.longitude, startLatitude, startLongitude) < limit
                ).collect(Collectors.toList());

        double minDistance = Double.MAX_VALUE;
        TaxiEntity minDistanceTaxi = null;
        ArrayList<TaxiEntity> matched = new ArrayList<>(matchList);

        if (matched.size() == 0) {
            return null;
        }
        for (TaxiEntity taxi : matched) {
            MatchEntity match = this.matchService.findMatch(taxi.id);
            if (match != null) {
                continue;
            }
            double distance = this.calculateDistance(startLatitude, startLongitude, taxi.latitude, taxi.longitude);
            if (distance < minDistance) {
                minDistance = distance;
                minDistanceTaxi = taxi;
            }
        }

        double estimateDistance = this.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);
        int estimateTime = this.calculateEstimateTime(startLatitude, startLongitude, endLatitude, endLongitude);
        int estimateCost = this.calculateEstimateCost(startLatitude, startLongitude, endLatitude, endLongitude);

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.estimateDistance = estimateDistance;
        matchEntity.estimateCost = estimateCost;
        matchEntity.estimateTime = estimateTime;
        matchEntity.startLatitude = startLatitude;
        matchEntity.startLongitude = startLongitude;
        matchEntity.endLatitude = endLatitude;
        matchEntity.endLongitude = endLongitude;
        matchEntity.taxiId = minDistanceTaxi.id;
        matchEntity.userId = userId;
        matchEntity.matchedAt = new Date();
        this.matchService.addMatch(matchEntity);

        return minDistanceTaxi;
    }

}

