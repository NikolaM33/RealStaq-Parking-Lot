package com.realstaq.loader;


import com.realstaq.domain.parking.ParkingLot;
import com.realstaq.repository.ParkingLotRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This component class is responsible for loading data from a CSV file into the system.
 * It reads the CSV file and maps each row to a corresponding entity object, which is then saved to the database.
 * <p>
 * The CSV file must have a specific format, with headers in the first row indicating the names of the columns.
 * <p>
 * The component provides a method to load the data from the CSV file and save it to the database.
 * <p>
 * Note that this component assumes that the database is already configured and accessible.
 */
@Component
@Slf4j
public class ParkingDataLoader {

    private static final String CSV_FILE_PATH = "src/main/resources/static/LA_Parking_Lot.csv";

    private static final String CSV_RECORD_LATITUDE = "Latitude";

    private static final String CSV_RECORD_LONGITUDE = "Longitude";

    private static final String CSV_RECORD_NAME = "A_Name";

    private static final String CSV_RECORD_YEAR = "Year";

    private static final String CSV_RECORD_TYPE = "Type";

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    /**
     * Loads parking data from a CSV file and saves it to the database.
     * Uses the CSV file path defined in CSV_FILE_PATH constant.
     * Deletes all existing parking lots before saving the new ones.
     * Uses Apache Commons CSV library to parse CSV records.
     * Expects the CSV records to have columns for latitude, longitude, name, type, and year.
     * Sets the latitude and longitude values as a GeoPoint object for each parking lot.
     * Saves the parking lots to the database using the parkingLotRepository.
     * Logs an error if there is an issue reading the CSV file.
     */
    @PostConstruct
    private void loadParkingLotsFromCSV() {
        List<ParkingLot> parkingLots = new ArrayList<>();
        clearDatabase();
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder().setHeader().build().parse(reader);
            for (CSVRecord record : records) {
                ParkingLot parkingLot = new ParkingLot();
                parkingLot.setCoordinate(new GeoPoint(Double.parseDouble(record.get(CSV_RECORD_LATITUDE)), Double.parseDouble(record.get(CSV_RECORD_LONGITUDE))));
                parkingLot.setName(record.get(CSV_RECORD_NAME));
                parkingLot.setType(record.get(CSV_RECORD_TYPE));
                parkingLot.setYear(Integer.parseInt(record.get(CSV_RECORD_YEAR)));
                parkingLots.add(parkingLot);
            }

            parkingLotRepository.saveAll(parkingLots);
        } catch (IOException e) {
            log.error("ERROR READING CSV FILE", e.getMessage());
        }

    }

    private void clearDatabase() {
        parkingLotRepository.deleteAll();
    }
}
