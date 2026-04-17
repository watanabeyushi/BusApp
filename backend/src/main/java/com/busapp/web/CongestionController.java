package com.busapp.web;

import com.busapp.service.CongestionService;
import com.busapp.web.dto.CongestionLogResponse;
import com.busapp.web.dto.CongestionPostRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/congestion")
public class CongestionController {

    private final CongestionService congestionService;

    public CongestionController(CongestionService congestionService) {
        this.congestionService = congestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CongestionLogResponse post(@Valid @RequestBody CongestionPostRequest body) {
        return congestionService.record(body);
    }

    @GetMapping("/recent")
    public List<CongestionLogResponse> recent(@RequestParam(defaultValue = "20") int limit) {
        return congestionService.recent(limit);
    }

    /** Latest congestion for the route/stop; 404 when no record exists. */
    @GetMapping("/latest")
    public ResponseEntity<CongestionLogResponse> latest(
            @RequestParam String routeId,
            @RequestParam String stopId) {
        return congestionService.latestForStop(routeId, stopId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
