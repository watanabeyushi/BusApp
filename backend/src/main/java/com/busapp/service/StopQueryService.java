package com.busapp.service;

import com.busapp.entity.BusSchedule;
import com.busapp.entity.TripDirection;
import com.busapp.repository.BusScheduleRepository;
import com.busapp.web.dto.StopInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StopQueryService {

    private final BusScheduleRepository busScheduleRepository;

    public StopQueryService(BusScheduleRepository busScheduleRepository) {
        this.busScheduleRepository = busScheduleRepository;
    }

    /** 路線・方向に属する停車地を、路線上の順で重複なく返す */
    @Transactional(readOnly = true)
    public List<StopInfoResponse> listStops(String routeId, TripDirection direction) {
        List<BusSchedule> rows = busScheduleRepository.findByRouteIdAndTripDirectionOrderByStopSequenceAsc(
                routeId, direction);
        Map<String, BusSchedule> byStop = new LinkedHashMap<>();
        for (BusSchedule b : rows) {
            byStop.putIfAbsent(b.getStopId(), b);
        }
        return byStop.values().stream()
                .sorted(Comparator.comparingInt(BusSchedule::getStopSequence))
                .map(b -> new StopInfoResponse(b.getStopId(), b.getStopName()))
                .toList();
    }
}
