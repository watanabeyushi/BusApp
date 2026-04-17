package com.busapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalTime;

/**
 * 同一便は {@link #tripId} で結ばれる。各路線・方向ごとに行き用・帰り用の時刻表を持つ。
 */
@Entity
@Table(name = "bus_schedules")
public class BusSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 同一バス便を表す識別子（停車地をまたいで共通） */
    @Column(nullable = false)
    private Long tripId;

    @Column(nullable = false, length = 64)
    private String routeId;

    @Column(nullable = false, length = 128)
    private String routeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_direction", nullable = false, length = 16)
    private TripDirection tripDirection;

    @Column(nullable = false, length = 64)
    private String stopId;

    @Column(nullable = false, length = 128)
    private String stopName;

    /** 便内の停車順（昇順＝路線の進行方向） */
    @Column(nullable = false)
    private int stopSequence;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DayType dayType;

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public TripDirection getTripDirection() {
        return tripDirection;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public DayType getDayType() {
        return dayType;
    }
}
