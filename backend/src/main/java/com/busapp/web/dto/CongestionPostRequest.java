package com.busapp.web.dto;

import com.busapp.entity.CongestionLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CongestionPostRequest(
        @NotBlank String routeId,
        @NotBlank String stopId,
        @NotNull CongestionLevel level
) {
}
