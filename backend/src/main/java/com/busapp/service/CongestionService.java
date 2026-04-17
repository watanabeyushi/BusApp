package com.busapp.service;

import com.busapp.entity.CongestionLevel;
import com.busapp.entity.CongestionLog;
import com.busapp.repository.CongestionLogRepository;
import com.busapp.web.dto.CongestionLogResponse;
import com.busapp.web.dto.CongestionPostRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CongestionService {

    private final CongestionLogRepository congestionLogRepository;
    private final Clock clock;

    public CongestionService(CongestionLogRepository congestionLogRepository, Clock clock) {
        this.congestionLogRepository = congestionLogRepository;
        this.clock = clock;
    }

    @Transactional
    public CongestionLogResponse record(CongestionPostRequest request) {
        Instant now = Instant.now(clock);
        CongestionLog log = new CongestionLog(
                request.routeId(),
                request.stopId(),
                request.level(),
                now);
        CongestionLog saved = congestionLogRepository.save(log);
        return CongestionLogResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<CongestionLogResponse> recent(int limit) {
        List<CongestionLog> all = congestionLogRepository.findTop50ByOrderByRecordedAtDesc();
        return all.stream()
                .limit(Math.max(1, Math.min(limit, 50)))
                .map(CongestionLogResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<CongestionLogResponse> latestForStop(String routeId, String stopId) {
        return congestionLogRepository
                .findFirstByRouteIdAndStopIdOrderByRecordedAtDesc(routeId, stopId)
                .map(CongestionLogResponse::from);
    }
}
