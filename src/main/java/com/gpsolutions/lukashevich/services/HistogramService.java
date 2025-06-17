package com.gpsolutions.lukashevich.services;

import java.util.Map;

public interface HistogramService {

  Map<String, Long> getHistogramByParameter(String param);
}
