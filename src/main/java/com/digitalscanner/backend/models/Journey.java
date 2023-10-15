package com.digitalscanner.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Journey {

    @Id
    private String id;
    private String start;
    private String end;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;
    private String busId;
    private String busType;
    private String route;
    private String routeNo;
    private String status = "ongoing";
    private Double distance;
    private Double charge = 0.0;
    private String user;
}
