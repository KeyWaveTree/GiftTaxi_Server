package com.gift.gifttaxi.server.service;

import com.gift.gifttaxi.server.model.MatchEntity;
import com.gift.gifttaxi.server.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService {
    private MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void addMatch(MatchEntity match) {
        this.matchRepository.save(match);
    }

    public MatchEntity findMatch(long taxiId) {
        Optional<MatchEntity> result = this.matchRepository.findMatchEntityByTaxiId(taxiId);
        if (!result.isPresent()) {
            return null;
        }
        return result.get();
    }
}
