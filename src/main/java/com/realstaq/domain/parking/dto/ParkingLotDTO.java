package com.realstaq.domain.parking.dto;

import lombok.Data;

@Data
public class ParkingLotDTO {

    private String id;

    private String name;

    private String type;

    private Integer year;

    private Double latitude;

    private Double longitude;
}
