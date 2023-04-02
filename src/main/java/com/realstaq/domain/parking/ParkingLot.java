package com.realstaq.domain.parking;

import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * A domain class representing a parking lot location.
 */
@Data
@Document(indexName = "parking-lot")
public class ParkingLot {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private int year;

    @Field(type = FieldType.Keyword)
    private String type;

    @GeoPointField
    private GeoPoint coordinate;
}
