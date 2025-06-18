package com.gpsolutions.lukashevich.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Value;

import java.util.Map;

@Value
public class HistogramResponse {
    Map<String, Integer> properties;

    @JsonAnyGetter
    public Map<String, Integer> getProperties() {
        return properties;
    }
}
