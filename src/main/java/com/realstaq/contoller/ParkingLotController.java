package com.realstaq.contoller;

import com.realstaq.configuration.error.BadRequestException;
import com.realstaq.domain.parking.dto.LocationDTO;
import com.realstaq.domain.parking.dto.ParkingLotDTO;
import com.realstaq.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for managing Parking Lots.
 *
 * This controller provides REST endpoints to perform search
 * operations on Parking Lots. It supports standard HTTP method GET
 * and returns responses in JSON format.
 *
 * Endpoints:
 * - GET /parking-lot/nearest: Return nearest parking lot to provided location
 * - GET /parking-lot/calculate-location-score: Calculate location parking score for provided location
 *
 * If an error occurs, the controller throws a BadRequestException with an error message
 * indicating the cause of the error.
 */

@RestController
@RequestMapping("/parking-lot")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    /**
     * GET endpoint to retrieve the nearest parking lot to a given location with latitude and longitude.
     * @throws BadRequestException if the latitude or longitude are not valid coordinates  and if no parking founded.
     *
     * @param locationDTO  The location with latitude and longitude  of the location to search from.
     * @return The nearest parking lot to the given location.
     */
    @GetMapping(value = "/nearest")
    public ResponseEntity<ParkingLotDTO> findNearestParkingLotByLocation (@Valid @RequestBody LocationDTO locationDTO){
        return new ResponseEntity<>(parkingLotService.findNearestParkingLotByLocation(locationDTO), HttpStatus.OK);

    }
    /**
     * Endpoint to calculate a parking score for a given location based on the number of parking lots within a 1km radius.
     *
     * @param locationDTO The location with longitude and latitude of the location.
     * @return  parking score.
     * @throws BadRequestException If the LocationDTO object is not valid.
     */
    @GetMapping(value = "/calculate-location-score")
    public ResponseEntity<Double> calculateLocationParkingScore (@Valid @RequestBody LocationDTO locationDTO){
        return  new ResponseEntity<>(parkingLotService.calculateLocationParkingScore(locationDTO),HttpStatus.OK);
    }
}
