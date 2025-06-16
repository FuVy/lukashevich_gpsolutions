package com.gpsolutions.lukashevich.utils.converters;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PairsConverter {
    public static <K, V> Map<K, V> toMap(List<Pair<K, V>> pairs) {
        return pairs.stream().collect(Collectors.toMap(
                Pair::getFirst,
                Pair::getSecond
        ));
    }
}
