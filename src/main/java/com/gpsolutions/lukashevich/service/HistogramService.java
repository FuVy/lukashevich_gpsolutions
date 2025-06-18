package com.gpsolutions.lukashevich.service;

import java.util.Map;

public interface HistogramService {

  Map<String, Long> getHistogramByParameter(String param);
}
