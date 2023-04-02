package com.realstaq.repository;

import com.realstaq.domain.parking.ParkingLot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository  extends ElasticsearchRepository<ParkingLot,String> {
}
