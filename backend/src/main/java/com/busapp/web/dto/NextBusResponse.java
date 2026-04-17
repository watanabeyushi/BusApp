package com.busapp.web.dto;

public record NextBusResponse(
        String routeId,
        String routeName,
        String boardingStopId,
        String destinationStopId,
        /** 乗車地での出発（HH:mm:ss） */
        String departureTime,
        /** 目的地への到着（HH:mm:ss） */
        String arrivalAtDestination,
        String followingDepartureTime,
        String followingArrivalAtDestination
) {
}
