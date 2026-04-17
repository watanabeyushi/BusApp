package com.busapp.web.dto;

public record NextBusResponse(
        String routeId,
        String routeName,
        String stopId,
        /** ISO-8601 local time, e.g. {@code 08:30:00} */
        String departureTime
) {
}
