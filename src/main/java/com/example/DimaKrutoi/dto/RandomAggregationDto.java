package com.example.DimaKrutoi.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RandomAggregationDto {
    String from;
    String to;
}
