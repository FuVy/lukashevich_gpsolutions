package com.gpsolutions.lukashevich.domain.search;

import lombok.Value;

@Value
public class FailedSearchItem {
    String field;
    String value;
    String message;
}
