package com.busapp.service;

import com.busapp.entity.TripDirection;
import com.busapp.web.dto.NextBusResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NextBusServiceTest {

    @Autowired
    private NextBusService nextBusService;

    @Test
    void outboundMainToOfficeFindsNextAndFollowing() {
        LocalDate friday = LocalDate.of(2026, 4, 17);
        Optional<NextBusResponse> r = nextBusService.findNext(
                "R1", "STOP_MAIN", "STOP_OFFICE", TripDirection.OUTBOUND, friday, LocalTime.of(7, 15));
        assertThat(r).isPresent();
        assertThat(r.get().departureTime()).isEqualTo("07:30:00");
        assertThat(r.get().arrivalAtDestination()).isEqualTo("08:00:00");
        // 平日かつ ALL ダイヤの便が日中にも入るため、2本目は 10:30 発（便301）
        assertThat(r.get().followingDepartureTime()).isEqualTo("10:30:00");
        assertThat(r.get().followingArrivalAtDestination()).isEqualTo("11:00:00");
        assertThat(r.get().routeName()).isEqualTo("中央線");
    }

    @Test
    void secondLegNullWhenOnlyOneTripLeft() {
        LocalDate friday = LocalDate.of(2026, 4, 17);
        Optional<NextBusResponse> r = nextBusService.findNext(
                "R1", "STOP_MAIN", "STOP_OFFICE", TripDirection.OUTBOUND, friday, LocalTime.of(22, 20));
        assertThat(r).isPresent();
        assertThat(r.get().departureTime()).isEqualTo("22:35:00");
        assertThat(r.get().followingDepartureTime()).isNull();
    }

    @Test
    void returnsEmptyWhenNoLaterDeparture() {
        LocalDate friday = LocalDate.of(2026, 4, 17);
        Optional<NextBusResponse> r = nextBusService.findNext(
                "R1", "STOP_MAIN", "STOP_OFFICE", TripDirection.OUTBOUND, friday, LocalTime.of(23, 0));
        assertThat(r).isEmpty();
    }

    @Test
    void emptyWhenBoardingEqualsDestination() {
        LocalDate friday = LocalDate.of(2026, 4, 17);
        Optional<NextBusResponse> r = nextBusService.findNext(
                "R1", "STOP_MAIN", "STOP_MAIN", TripDirection.OUTBOUND, friday, LocalTime.of(7, 0));
        assertThat(r).isEmpty();
    }

    @Test
    void inboundOfficeToMain() {
        LocalDate friday = LocalDate.of(2026, 4, 17);
        Optional<NextBusResponse> r = nextBusService.findNext(
                "R1", "STOP_OFFICE", "STOP_MAIN", TripDirection.INBOUND, friday, LocalTime.of(16, 30));
        assertThat(r).isPresent();
        assertThat(r.get().departureTime()).isEqualTo("17:00:00");
        assertThat(r.get().arrivalAtDestination()).isEqualTo("17:30:00");
    }
}
