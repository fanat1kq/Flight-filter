package com.gridnine.service;


import com.gridnine.entity.Flight;

import java.util.List;

public interface FlightFilterBuilder {

  List<Flight> build();

  FlightFilterBuilder filterDepartureBeforeNow();

  FlightFilterBuilder filterArrivalBeforeDeparture();

  FlightFilterBuilder filterSumTimeOnGroundMoreThanTwoHours();
}
