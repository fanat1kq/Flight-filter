package com.gridnine;

import com.gridnine.dao.FlightBuilder;
import com.gridnine.entity.Flight;
import com.gridnine.service.FlightFilterBuilderImpl;

import java.util.List;

/**
 * @author fanat1kq
 * Created on 31.05.2024
 */

public class Main {

  public static void main(String[] args) {

    List<Flight> flights = FlightBuilder.createFlights();
	List<Flight> flightsDepartureBeforeNow = new FlightFilterBuilderImpl(flights)
			.filterDepartureBeforeNow()
			.build();
	List<Flight> flightsArrivalBeforeDeparture = new FlightFilterBuilderImpl(flights)
			.filterArrivalBeforeDeparture()
			.build();
	List<Flight> flightsTimeOnGroundMoreThanTwoHours = new FlightFilterBuilderImpl(flights)
			.filterSumTimeOnGroundMoreThanTwoHours()
			.build();

	System.out.println("Unfiltered flies:\n" + flights);
	System.out.println("Departure before now:\n" + flightsDepartureBeforeNow);
	System.out.println("Arrival before departure:\n" + flightsArrivalBeforeDeparture);
	System.out.println("Time on the ground is more than two hours:\n" + flightsTimeOnGroundMoreThanTwoHours);
  }
}
