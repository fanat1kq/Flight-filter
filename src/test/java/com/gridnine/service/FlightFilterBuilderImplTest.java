package com.gridnine.service;

import com.gridnine.entity.Flight;
import com.gridnine.entity.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightFilterBuilderImplTest {

  static LocalDateTime inTwoHours = LocalDateTime.now().plusHours(2);
  static LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

  List<Flight> normalFlightsAfterNow;
  List<Flight> normalFlightsDeparturesBeforeNow;
  List<Flight> moreThanTwoHoursOnGroundFlights;
  List<Flight> arrivalBeforeDeparture;
  List<Flight> testFlights;

  public void initNormalFlightsInAfterNow() {
	normalFlightsAfterNow = new ArrayList<>();
	LocalDateTime inTwoHours = LocalDateTime.now().plusHours(2);
	LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

	//single-segment flight with 4 hours duration
	List<Segment> normalSingleSegment = new ArrayList<>();
	normalSingleSegment.add(new Segment(inTwoHours, inTwoHours.plusHours(4)));
	Flight normalSingleSegmentFlight = new Flight(normalSingleSegment);

	//multi-segments flight with 4 and 5 hours durations, and one hour and 30 minutes on the ground
	List<Segment> normalMultiSegments = new ArrayList<>();
	normalMultiSegments.add(new Segment(tomorrow, tomorrow.plusHours(4)));
	normalMultiSegments.add(new Segment(tomorrow.plusHours(5), tomorrow.plusHours(10)));
	normalMultiSegments.add(new Segment(tomorrow.plusHours(10).plusMinutes(30), tomorrow.plusHours(14)));
	Flight normalMultiSegmentFlight = new Flight(normalMultiSegments);

	normalFlightsAfterNow.add(normalSingleSegmentFlight);
	normalFlightsAfterNow.add(normalMultiSegmentFlight);
  }

  public void initNormalFlightsDeparturesBeforeNow() {
	normalFlightsDeparturesBeforeNow = new ArrayList<>();
	LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
	LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

	//single-segment flight with 4 hours duration, departure yesterday
	List<Segment> singleSegment = new ArrayList<>();
	singleSegment.add(new Segment(yesterday, yesterday.plusHours(4)));
	Flight singleSegmentFlight = new Flight(singleSegment);

	//single-segment flight with departure 5 hours ago and arrival in 3 hours
	List<Segment> singleSegmentFromPastToFuture = new ArrayList<>();
	singleSegmentFromPastToFuture.add(new Segment(LocalDateTime.now().minusHours(5), LocalDateTime.now().plusHours(3)));
	Flight normalSingleSegmentFromYesterdayToTodayFlight = new Flight(singleSegmentFromPastToFuture);

	//multi-segments flight with 4 and 4.5 hours durations, and one hour and 30 minutes on the ground,
	//departure three days ago
	List<Segment> normalMultiSegments = new ArrayList<>();
	normalMultiSegments.add(new Segment(threeDaysAgo, threeDaysAgo.plusHours(4)));
	normalMultiSegments.add(new Segment(threeDaysAgo.plusHours(5).plusMinutes(30), threeDaysAgo.plusHours(10)));
	Flight normalMultiSegmentFlight = new Flight(normalMultiSegments);

	normalFlightsDeparturesBeforeNow.add(singleSegmentFlight);
	normalFlightsDeparturesBeforeNow.add(normalSingleSegmentFromYesterdayToTodayFlight);
	normalFlightsDeparturesBeforeNow.add(normalMultiSegmentFlight);
  }

  public void initMoreThanTwoHoursOnGroundFlights() {
	moreThanTwoHoursOnGroundFlights = new ArrayList<>();
	//flight with two hours and one minute on the ground
	List<Segment> multiSegmentMoreThanOneHour = new ArrayList<>();
	multiSegmentMoreThanOneHour.add(new Segment(inTwoHours, inTwoHours.plusHours(4)));
	multiSegmentMoreThanOneHour.add(new Segment(inTwoHours.plusHours(6).plusMinutes(1), inTwoHours.plusHours(10)));
	Flight singleSegmentFlight = new Flight(multiSegmentMoreThanOneHour);

	//flight with one and two hours on the ground
	List<Segment> multiSegmentsThreeHour = new ArrayList<>();
	multiSegmentsThreeHour.add(new Segment(tomorrow, tomorrow.plusHours(2)));
	multiSegmentsThreeHour.add(new Segment(tomorrow.plusHours(3), tomorrow.plusHours(7)));
	multiSegmentsThreeHour.add(new Segment(tomorrow.plusHours(8).plusMinutes(1), tomorrow.plusHours(12)));
	Flight multiSegmentFlight = new Flight(multiSegmentsThreeHour);

	moreThanTwoHoursOnGroundFlights.add(singleSegmentFlight);
	moreThanTwoHoursOnGroundFlights.add(multiSegmentFlight);
  }

  public void initArrivalBeforeDeparture() {
	arrivalBeforeDeparture = new ArrayList<>();
	//flight that arrives tomorrow four hours before departure
	List<Segment> singleSegment = new ArrayList<>();
	singleSegment.add(new Segment(tomorrow, tomorrow.minusHours(4)));
	Flight singleSegmentFlight = new Flight(singleSegment);

	//multi-segment flight flying into the past, one hour on ground
	List<Segment> multiSegment = new ArrayList<>();
	multiSegment.add(new Segment(tomorrow, tomorrow.minusHours(4)));
	multiSegment.add(new Segment(tomorrow.minusHours(5), tomorrow.minusHours(10)));
	Flight multiSegmentFlight = new Flight(multiSegment);

	arrivalBeforeDeparture.add(singleSegmentFlight);
	arrivalBeforeDeparture.add(multiSegmentFlight);
  }

  @BeforeEach
  public void initTestFlights() {
	testFlights = new ArrayList<>();

	initNormalFlightsInAfterNow();
	initNormalFlightsDeparturesBeforeNow();
	initMoreThanTwoHoursOnGroundFlights();
	initArrivalBeforeDeparture();

	testFlights.addAll(normalFlightsAfterNow);
	testFlights.addAll(normalFlightsDeparturesBeforeNow);
	testFlights.addAll(moreThanTwoHoursOnGroundFlights);
	testFlights.addAll(arrivalBeforeDeparture);
  }

  @Test
  public void filterDepartureBeforeNow_onlyNormalFlightsDeparturesBeforeNow() {
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(normalFlightsDeparturesBeforeNow);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterDepartureBeforeNow()
			.build();
	assertTrue(filteredFlights.isEmpty());
  }

  @Test
  public void filterDepartureBeforeNow_allTestFlights() {
	List<Flight> expectedFlights = new ArrayList<>(testFlights);
	expectedFlights.removeAll(normalFlightsDeparturesBeforeNow);
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(testFlights);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterDepartureBeforeNow()
			.build();
	assertEquals(expectedFlights, filteredFlights);
  }

  @Test
  public void filterArrivalBeforeDeparture_onlyArrivalBeforeDeparture() {
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(arrivalBeforeDeparture);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterArrivalBeforeDeparture()
			.build();
	assertTrue(filteredFlights.isEmpty());
  }

  @Test
  public void filterArrivalBeforeDeparture_allTestFlights() {
	List<Flight> expectedFlights = new ArrayList<>(testFlights);
	expectedFlights.removeAll(arrivalBeforeDeparture);
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(testFlights);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterArrivalBeforeDeparture()
			.build();
	assertEquals(expectedFlights, filteredFlights);
  }


  @Test
  public void filterSumTimeOnGroundMoreThanTwoHours_onlyMoreThanTwoHoursOnGroundFlights() {
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(moreThanTwoHoursOnGroundFlights);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterSumTimeOnGroundMoreThanTwoHours()
			.build();
	assertTrue(filteredFlights.isEmpty());
  }

  @Test
  public void filterSumTimeOnGroundMoreThanTwoHours_allTestFlights() {
	List<Flight> expectedFlights = new ArrayList<>(testFlights);
	expectedFlights.removeAll(moreThanTwoHoursOnGroundFlights);
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(testFlights);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterSumTimeOnGroundMoreThanTwoHours()
			.build();
	assertEquals(expectedFlights, filteredFlights);
  }

  @Test
  public void allFilters() {
	List<Flight> expectedFlights = normalFlightsAfterNow;
	FlightFilterBuilder flightFilterBuilder = new FlightFilterBuilderImpl(testFlights);

	List<Flight> filteredFlights = flightFilterBuilder
			.filterDepartureBeforeNow()
			.filterArrivalBeforeDeparture()
			.filterSumTimeOnGroundMoreThanTwoHours()
			.build();
	assertEquals(expectedFlights, filteredFlights);
  }
}
