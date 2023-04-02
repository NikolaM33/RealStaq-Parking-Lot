package com.realstaq.service;

import com.realstaq.domain.parking.dto.LocationDTO;
import com.realstaq.domain.parking.dto.ParkingLotDTO;


public interface ParkingLotService {

    ParkingLotDTO  findNearestParkingLotByLocation (LocationDTO locationDTO);

    Double calculateLocationParkingScore(LocationDTO locationDTO);
}
