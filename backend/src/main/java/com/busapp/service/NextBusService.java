package com.busapp.service;

import com.busapp.entity.BusSchedule;
import com.busapp.entity.DayType;
import com.busapp.entity.TripDirection;
import com.busapp.repository.BusScheduleRepository;
import com.busapp.web.dto.NextBusResponse;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class NextBusService {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final BusScheduleRepository busScheduleRepository;
    private final Clock clock;

    public NextBusService(BusScheduleRepository busScheduleRepository, Clock clock) {
        this.busScheduleRepository = busScheduleRepository;
        this.clock = clock;
    }

    public Optional<NextBusResponse> findNext(
            String routeId,
            String boardingStopId,
            String destinationStopId,
            TripDirection direction) {
        ZonedDateTime now = ZonedDateTime.now(clock);
        return findNext(routeId, boardingStopId, destinationStopId, direction, now.toLocalDate(), now.toLocalTime());
    }

    public Optional<NextBusResponse> findNext(
            String routeId,
            String boardingStopId,
            String destinationStopId,
            TripDirection direction,
            LocalDate date,
            LocalTime after) {
        if (boardingStopId.equals(destinationStopId)) {
            return Optional.empty();
        }
        List<DayType> dayTypes = DayTypeResolver.applicableDayTypes(date);
        List<BusSchedule> atBoarding = busScheduleRepository
                .findByRouteIdAndTripDirectionAndStopIdAndDepartureTimeAfterAndDayTypeInOrderByDepartureTimeAsc(
                        routeId, direction, boardingStopId, after, dayTypes);
        if (atBoarding.isEmpty()) {
            return Optional.empty();
        }
        Optional<NextBusResponse> first = buildLeg(atBoarding.get(0), destinationStopId);
        if (first.isEmpty()) {
            return Optional.empty();
        }
        String followingDep = null;
        String followingArr = null;
        if (atBoarding.size() > 1) {
            Optional<NextBusResponse> second = buildLeg(atBoarding.get(1), destinationStopId);
            if (second.isPresent()) {
                followingDep = second.get().departureTime();
                followingArr = second.get().arrivalAtDestination();
            }
        }
        NextBusResponse one = first.get();
        return Optional.of(new NextBusResponse(
                one.routeId(),
                one.routeName(),
                one.boardingStopId(),
                one.destinationStopId(),
                one.departureTime(),
                one.arrivalAtDestination(),
                followingDep,
                followingArr));
    }

    private Optional<NextBusResponse> buildLeg(BusSchedule boardingRow, String destinationStopId) {
        Optional<BusSchedule> destOpt = busScheduleRepository.findByTripIdAndStopId(
                boardingRow.getTripId(), destinationStopId);
        if (destOpt.isEmpty()) {
            return Optional.empty();
        }
        BusSchedule dest = destOpt.get();
        if (dest.getStopSequence() <= boardingRow.getStopSequence()) {
            return Optional.empty();
        }
        return Optional.of(new NextBusResponse(
                boardingRow.getRouteId(),
                boardingRow.getRouteName(),
                boardingRow.getStopId(),
                destinationStopId,
                boardingRow.getDepartureTime().format(TIME_FORMAT),
                dest.getArrivalTime().format(TIME_FORMAT),
                null,
                null));
    }
}
