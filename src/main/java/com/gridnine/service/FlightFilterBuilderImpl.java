package com.gridnine.service;

import com.gridnine.entity.Flight;
import com.gridnine.entity.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightFilterBuilderImpl implements FlightFilterBuilder {

  private final List<Flight> flights;

  public FlightFilterBuilderImpl(List<Flight> flights) {
	this.flights = new ArrayList<>(flights);
  }

  @Override
  public List<Flight> build() {
	return flights;
  }

  @Override
  public FlightFilterBuilder filterDepartureBeforeNow() {
	flights.removeIf(flight ->
			flight.getSegments().stream()
					.anyMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now()))
	);
	return this;
  }

  @Override
  public FlightFilterBuilder filterArrivalBeforeDeparture() {
	flights.removeIf(flight ->
			flight.getSegments().stream()
					.anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()))
	);
	return this;
  }

  @Override
  public FlightFilterBuilder filterSumTimeOnGroundMoreThanTwoHours() {
	flights.removeIf(flight -> {
	  List<Segment> segments = flight.getSegments();
	  LocalDateTime curDeparture;
	  LocalDateTime lastArrival;
	  Duration duration = Duration.ZERO;

	  for (int i = 1; i < segments.size(); i++) {
		curDeparture = segments.get(i).getDepartureDate();
		lastArrival = segments.get(i - 1).getArrivalDate();
		duration = duration.plus(Duration.between(curDeparture, lastArrival).abs());
	  }
	  return duration.toHours() >= 2;
	});
	return this;
  }
}
