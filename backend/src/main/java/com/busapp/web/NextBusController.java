package com.busapp.web;

import com.busapp.service.NextBusService;
import com.busapp.web.dto.NextBusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bus")
public class NextBusController {

    private final NextBusService nextBusService;

    public NextBusController(NextBusService nextBusService) {
        this.nextBusService = nextBusService;
    }

    @GetMapping("/next")
    public ResponseEntity<NextBusResponse> next(
            @RequestParam String routeId,
            @RequestParam String stopId) {
        return nextBusService.findNext(routeId, stopId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
