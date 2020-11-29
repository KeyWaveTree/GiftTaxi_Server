package com.gift.gifttaxi.server.controller;

import com.gift.gifttaxi.server.dto.MatchDto;
import com.gift.gifttaxi.server.dto.MatchResultDto;
import com.gift.gifttaxi.server.dto.TaxiDto;
import com.gift.gifttaxi.server.dto.TaxiListDto;
import com.gift.gifttaxi.server.model.TaxiEntity;
import com.gift.gifttaxi.server.model.UserEntity;
import com.gift.gifttaxi.server.service.TaxiService;
import com.gift.gifttaxi.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController()
@RequestMapping(path = "/taxis")
public class TaxiController {
    private TaxiService taxiService;
    private UserService userService;

    @Autowired
    public TaxiController(TaxiService taxiService, UserService userService) {
        this.taxiService = taxiService;
        this.userService = userService;
    }

    @GetMapping
    public TaxiListDto getTaxiList() {
        ArrayList<TaxiEntity> taxis = this.taxiService.findTaxiList();
        ArrayList<TaxiDto> list = new ArrayList();
        for (TaxiEntity taxi : taxis) {
            TaxiDto dto = new TaxiDto();
            dto.driver = taxi.driver;
            dto.taxiNumber = taxi.taxiNumber;
            dto.latitude = taxi.latitude;
            dto.longitude = taxi.longitude;
            list.add(dto);
        }
        TaxiListDto result = new TaxiListDto();
        result.taxis = list;
        return result;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addTaxi(@RequestBody() TaxiDto dto) {
        TaxiEntity taxi = new TaxiEntity();
        taxi.driver = dto.driver;
        taxi.taxiNumber = dto.taxiNumber;
        taxi.latitude = dto.latitude;
        taxi.longitude = dto.longitude;
        this.taxiService.addTaxiPosition(taxi);
    }

    @PostMapping("/match")
    public MatchResultDto matchTaxi(@RequestBody() MatchDto dto) {
        UserEntity user = this.userService.findUserById(dto.userId);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        TaxiEntity match = this.taxiService.match(
                dto.startLatitude,
                dto.startLongitude,
                dto.endLatitude,
                dto.endLongitude,
                user.id,
                dto.limitTime
        );
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        int estimateTime = this.taxiService.calculateEstimateTime(match.latitude, match.longitude,
                dto.startLatitude, dto.startLongitude);
        MatchResultDto result = new MatchResultDto();
        result.taxiNumber = match.taxiNumber;
        result.driver = match.driver;
        result.arrivalTime = estimateTime;
        return result;
    }
}

