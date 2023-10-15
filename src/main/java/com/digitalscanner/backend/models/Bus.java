package com.digitalscanner.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Buses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
    @Id
    private String id;
    private String busType;
    private Integer securityCode;
    private Integer numOfSeats;
    private String busDriverId;

}
