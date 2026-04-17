package com.busapp.repository;

import com.busapp.entity.CongestionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CongestionLogRepository extends JpaRepository<CongestionLog, Long> {

    List<CongestionLog> findTop50ByOrderByRecordedAtDesc();

    Optional<CongestionLog> findFirstByRouteIdAndStopIdOrderByRecordedAtDesc(String routeId, String stopId);
}
