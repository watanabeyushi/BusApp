package com.busapp.web.dto;

import com.busapp.entity.CongestionLog;

public record CongestionLogResponse(
        Long id,
        String routeId,
        String stopId,
        String level,
        String recordedAt
) {
    public static CongestionLogResponse from(CongestionLog log) {
        return new CongestionLogResponse(
                log.getId(),
                log.getRouteId(),
                log.getStopId(),
                log.getLevel().name(),
                log.getRecordedAt().toString());
    }
}
