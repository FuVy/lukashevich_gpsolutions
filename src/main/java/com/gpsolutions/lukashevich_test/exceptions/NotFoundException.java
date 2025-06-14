package com.gpsolutions.lukashevich_test.exceptions;

import com.gpsolutions.lukashevich_test.domain.search.FailedSearchItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotFoundException {
    protected List<FailedSearchItem> failedItems;
}
