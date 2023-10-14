package com.digitalscanner.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fare {
    @Id
    private String busType;
    private Double priceFirst5Km;
    private Double price5KmTo20Km;
    private Double priceMoreThan20Km;
}
