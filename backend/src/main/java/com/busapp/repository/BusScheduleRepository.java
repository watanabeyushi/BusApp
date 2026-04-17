package com.busapp.repository;

import com.busapp.entity.BusSchedule;
import com.busapp.entity.DayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {

    @Query("""
            SELECT b FROM BusSchedule b
            WHERE b.routeId = :routeId
              AND b.stopId = :stopId
              AND b.departureTime > :after
              AND b.dayType IN :dayTypes
            ORDER BY b.departureTime ASC
            """)
    List<BusSchedule> findNextDepartures(
            @Param("routeId") String routeId,
            @Param("stopId") String stopId,
            @Param("after") LocalTime after,
            @Param("dayTypes") List<DayType> dayTypes);
}
