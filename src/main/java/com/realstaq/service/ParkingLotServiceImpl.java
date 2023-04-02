package com.realstaq.service;


import com.realstaq.configuration.error.BadRequestException;
import com.realstaq.domain.parking.ParkingLot;
import com.realstaq.domain.parking.dto.LocationDTO;
import com.realstaq.domain.parking.dto.ParkingLotDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;

/**
 * Implements the business logic for managing parking lots, using the {@link ElasticsearchOperations} to
 * interact with the database. Provides methods for calculating the parking score for a given location,
 */

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ElasticsearchOperations elasticsearchOperations;


    /**
     * Implementation of the ParkingLotService interface that uses Elasticsearch operations to find the nearest parking lot to a given location.
     *
     * @param locationDTO The location for which the parking score needs to be calculated.
     * @return ParkingLotDTO object for nearest founded parking lot.
     */
    @Override
    public ParkingLotDTO findNearestParkingLotByLocation(LocationDTO locationDTO) {

        NativeSearchQueryBuilder nearestParkingLotQuery = new NativeSearchQueryBuilder();

        GeoPoint location = new GeoPoint(locationDTO.getLatitude(), locationDTO.getLongitude());

        GeoDistanceSortBuilder sortByGeoLocationQuery = SortBuilders.geoDistanceSort("coordinate", location);
        sortByGeoLocationQuery.unit(DistanceUnit.METERS);
        sortByGeoLocationQuery.order(SortOrder.ASC);
        sortByGeoLocationQuery.point(locationDTO.getLatitude(), locationDTO.getLongitude());
        sortByGeoLocationQuery.geoDistance(GeoDistance.ARC);

        nearestParkingLotQuery.withQuery(QueryBuilders.matchAllQuery());
        nearestParkingLotQuery.withSorts(sortByGeoLocationQuery);
        nearestParkingLotQuery.withPageable(PageRequest.of(0, 1));

        SearchHits<ParkingLot> searchHits = elasticsearchOperations.search(nearestParkingLotQuery.build(), ParkingLot.class);
        if (!searchHits.isEmpty()) {
            return convertToParkingLotDTO(searchHits.getSearchHit(0).getContent());
        } else {
            throw new BadRequestException("PARKING_LOT_NOT_FOUND");
        }
    }


    /**
     * Calculates the location parking score for a given location.
     * This method uses ElasticsearchOperations to query the ParkingLot index and count the number of parking lots
     * within a 1km radius of the given location. It then calculates the parking score as the ratio of parking lots
     * within 1km radius to the total number of parking lots.
     *
     * @param locationDTO The location for which the parking score needs to be calculated.
     * @return The location parking score as a Double value.
     */
    @Override
    public Double calculateLocationParkingScore(LocationDTO locationDTO) {
        GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("coordinate")
                .point(new GeoPoint(locationDTO.getLatitude(), locationDTO.getLongitude()))
                .distance(1, DistanceUnit.KILOMETERS)
                .geoDistance(GeoDistance.ARC);

        NativeSearchQuery findParkingLotIn1kmRadiusQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        long numberOfParkingLotIn1km = elasticsearchOperations.count(findParkingLotIn1kmRadiusQuery, ParkingLot.class);
        long totalParkingLots = elasticsearchOperations.count(new NativeSearchQueryBuilder().build(), ParkingLot.class);
        if (totalParkingLots == 0) {
            return 0.0;
        } else {
            DecimalFormat df = new DecimalFormat("#.####");
            return Double.parseDouble(df.format((double) numberOfParkingLotIn1km / totalParkingLots));
        }
    }

    private ParkingLotDTO convertToParkingLotDTO(ParkingLot parkingLot) {
        ParkingLotDTO parkingLotDTO = new ParkingLotDTO();
        parkingLotDTO.setId(parkingLot.getId());
        parkingLotDTO.setName(parkingLot.getName());
        parkingLotDTO.setYear(parkingLot.getYear());
        parkingLotDTO.setType(parkingLot.getType());
        parkingLotDTO.setLatitude(parkingLot.getCoordinate().getLat());
        parkingLotDTO.setLongitude(parkingLot.getCoordinate().getLon());
        return parkingLotDTO;
    }
}
