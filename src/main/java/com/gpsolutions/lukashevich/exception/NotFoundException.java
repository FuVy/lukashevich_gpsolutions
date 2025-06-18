package com.gpsolutions.lukashevich.exception;

import com.gpsolutions.lukashevich.domain.search.FailedSearchItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    protected List<FailedSearchItem> failedItems;
}
