package com.busapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "congestion_logs")
public class CongestionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String routeId;

    @Column(nullable = false, length = 64)
    private String stopId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private CongestionLevel level;

    @Column(nullable = false)
    private Instant recordedAt;

    protected CongestionLog() {
    }

    public CongestionLog(String routeId, String stopId, CongestionLevel level, Instant recordedAt) {
        this.routeId = routeId;
        this.stopId = stopId;
        this.level = level;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public CongestionLevel getLevel() {
        return level;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}
