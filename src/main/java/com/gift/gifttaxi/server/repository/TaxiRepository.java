package com.gift.gifttaxi.server.repository;

import com.gift.gifttaxi.server.model.TaxiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxiRepository extends JpaRepository<TaxiEntity, Long> {

}
