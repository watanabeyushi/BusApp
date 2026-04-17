package com.busapp.web;

import com.busapp.entity.TripDirection;
import com.busapp.service.StopQueryService;
import com.busapp.web.dto.StopInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class StopsController {

    private final StopQueryService stopQueryService;

    public StopsController(StopQueryService stopQueryService) {
        this.stopQueryService = stopQueryService;
    }

    @GetMapping
    public List<StopInfoResponse> list(
            @RequestParam String routeId,
            @RequestParam TripDirection direction) {
        return stopQueryService.listStops(routeId, direction);
    }
}
