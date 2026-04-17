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

@Entity
@Table(name = "bus_schedules")
public class BusSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String routeId;

    @Column(nullable = false, length = 128)
    private String routeName;

    @Column(nullable = false, length = 64)
    private String stopId;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DayType dayType;

    public Long getId() {
        return id;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getStopId() {
        return stopId;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public DayType getDayType() {
        return dayType;
    }
}
