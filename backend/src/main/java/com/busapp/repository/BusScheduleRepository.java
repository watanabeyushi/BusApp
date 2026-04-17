package com.busapp.repository;

import com.busapp.entity.BusSchedule;
import com.busapp.entity.DayType;
import com.busapp.entity.TripDirection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {

    List<BusSchedule> findByRouteIdAndTripDirectionAndStopIdAndDepartureTimeAfterAndDayTypeInOrderByDepartureTimeAsc(
            String routeId,
            TripDirection tripDirection,
            String stopId,
            LocalTime after,
            List<DayType> dayTypes);

    Optional<BusSchedule> findByTripIdAndStopId(Long tripId, String stopId);

    List<BusSchedule> findByRouteIdAndTripDirectionOrderByStopSequenceAsc(String routeId, TripDirection tripDirection);
}
