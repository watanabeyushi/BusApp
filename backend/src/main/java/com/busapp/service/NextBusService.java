package com.busapp.service;

import com.busapp.entity.BusSchedule;
import com.busapp.entity.DayType;
import com.busapp.repository.BusScheduleRepository;
import com.busapp.web.dto.NextBusResponse;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NextBusService {

    private final BusScheduleRepository busScheduleRepository;
    private final Clock clock;

    public NextBusService(BusScheduleRepository busScheduleRepository, Clock clock) {
        this.busScheduleRepository = busScheduleRepository;
        this.clock = clock;
    }

    /**
     * Next departure after "now" (from {@link Clock}) for the given route and stop.
     */
    public Optional<NextBusResponse> findNext(String routeId, String stopId) {
        ZonedDateTime now = ZonedDateTime.now(clock);
        return findNext(routeId, stopId, now.toLocalDate(), now.toLocalTime());
    }

    /** Visible for tests: fixed date/time without relying on system clock. */
    public Optional<NextBusResponse> findNext(String routeId, String stopId, LocalDate date, LocalTime after) {
        List<DayType> dayTypes = DayTypeResolver.applicableDayTypes(date);
        List<BusSchedule> candidates = busScheduleRepository.findNextDepartures(routeId, stopId, after, dayTypes);
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        BusSchedule first = candidates.get(0);
        return Optional.of(new NextBusResponse(
                first.getRouteId(),
                first.getRouteName(),
                first.getStopId(),
                first.getDepartureTime().toString()));
    }
}
