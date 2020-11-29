package com.gift.gifttaxi.server.repository;

import com.gift.gifttaxi.server.model.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    Optional<MatchEntity> findMatchEntityByTaxiId(Long taxiId);
}
