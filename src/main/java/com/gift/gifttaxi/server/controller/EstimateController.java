package com.gift.gifttaxi.server.controller;

import com.gift.gifttaxi.server.dto.EstimateResultDto;
import com.gift.gifttaxi.server.service.TaxiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/estimate")
public class EstimateController {

    private TaxiService taxiService;

    @Autowired
    public EstimateController(TaxiService taxiService) {
        this.taxiService = taxiService;
    }

    @GetMapping()
    public EstimateResultDto getEstimate(@RequestParam("startLatitude") double startLatitude,
                                         @RequestParam("startLongitude") double startLongitude,
                                         @RequestParam("endLatitude") double endLatitude,
                                         @RequestParam("endLongitude") double endLongitude) {
        int estimateTime = this.taxiService.calculateEstimateTime(startLatitude, startLongitude, endLatitude, endLongitude);
        int estimateCost = this.taxiService.calculateEstimateCost(startLatitude, startLongitude, endLatitude, endLongitude);
        double estimateDistance = this.taxiService.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);
        EstimateResultDto dto = new EstimateResultDto();
        dto.estimateTime = estimateTime;
        dto.estimateCost = estimateCost;
        dto.estimateDistance = estimateDistance;
        return dto;
    }
}