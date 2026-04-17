-- 路線 R1: 行き（OUTBOUND） HOME -> MAIN -> OFFICE / 帰り（INBOUND） OFFICE -> MAIN -> HOME
-- trip_id で同一便を結ぶ。各停車地に arrival / departure を保持。

-- 行き便 101（平日） HOME 06:30 発 → MAIN 07:00 着発 → OFFICE 07:30 着
INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(1, 101, 'R1', '中央線', 'OUTBOUND', 'STOP_HOME', '自宅前', 1, '06:25:00', '06:30:00', 'WEEKDAY'),
(2, 101, 'R1', '中央線', 'OUTBOUND', 'STOP_MAIN', 'メイン通り', 2, '07:00:00', '07:00:00', 'WEEKDAY'),
(3, 101, 'R1', '中央線', 'OUTBOUND', 'STOP_OFFICE', 'オフィス前', 3, '07:30:00', '07:30:00', 'WEEKDAY');

-- 行き便 102
INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(4, 102, 'R1', '中央線', 'OUTBOUND', 'STOP_HOME', '自宅前', 1, '06:55:00', '07:00:00', 'WEEKDAY'),
(5, 102, 'R1', '中央線', 'OUTBOUND', 'STOP_MAIN', 'メイン通り', 2, '07:30:00', '07:30:00', 'WEEKDAY'),
(6, 102, 'R1', '中央線', 'OUTBOUND', 'STOP_OFFICE', 'オフィス前', 3, '08:00:00', '08:00:00', 'WEEKDAY');

-- 行き便 103（最終系）
INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(7, 103, 'R1', '中央線', 'OUTBOUND', 'STOP_HOME', '自宅前', 1, '22:00:00', '22:05:00', 'WEEKDAY'),
(8, 103, 'R1', '中央線', 'OUTBOUND', 'STOP_MAIN', 'メイン通り', 2, '22:35:00', '22:35:00', 'WEEKDAY'),
(9, 103, 'R1', '中央線', 'OUTBOUND', 'STOP_OFFICE', 'オフィス前', 3, '23:05:00', '23:05:00', 'WEEKDAY');

-- 土曜の行き 201
INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(10, 201, 'R1', '中央線', 'OUTBOUND', 'STOP_HOME', '自宅前', 1, '08:55:00', '09:00:00', 'SATURDAY'),
(11, 201, 'R1', '中央線', 'OUTBOUND', 'STOP_MAIN', 'メイン通り', 2, '09:30:00', '09:30:00', 'SATURDAY'),
(12, 201, 'R1', '中央線', 'OUTBOUND', 'STOP_OFFICE', 'オフィス前', 3, '10:00:00', '10:00:00', 'SATURDAY');

-- 全日ダイヤの行き 301
INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(13, 301, 'R1', '中央線', 'OUTBOUND', 'STOP_HOME', '自宅前', 1, '09:55:00', '10:00:00', 'ALL'),
(14, 301, 'R1', '中央線', 'OUTBOUND', 'STOP_MAIN', 'メイン通り', 2, '10:30:00', '10:30:00', 'ALL'),
(15, 301, 'R1', '中央線', 'OUTBOUND', 'STOP_OFFICE', 'オフィス前', 3, '11:00:00', '11:00:00', 'ALL');

-- 帰り便 401 INBOUND OFFICE -> MAIN -> HOME
INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(16, 401, 'R1', '中央線', 'INBOUND', 'STOP_OFFICE', 'オフィス前', 1, '17:00:00', '17:00:00', 'WEEKDAY'),
(17, 401, 'R1', '中央線', 'INBOUND', 'STOP_MAIN', 'メイン通り', 2, '17:30:00', '17:30:00', 'WEEKDAY'),
(18, 401, 'R1', '中央線', 'INBOUND', 'STOP_HOME', '自宅前', 3, '18:00:00', '18:00:00', 'WEEKDAY');

INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(19, 402, 'R1', '中央線', 'INBOUND', 'STOP_OFFICE', 'オフィス前', 1, '18:00:00', '18:00:00', 'WEEKDAY'),
(20, 402, 'R1', '中央線', 'INBOUND', 'STOP_MAIN', 'メイン通り', 2, '18:30:00', '18:30:00', 'WEEKDAY'),
(21, 402, 'R1', '中央線', 'INBOUND', 'STOP_HOME', '自宅前', 3, '19:00:00', '19:00:00', 'WEEKDAY');

INSERT INTO bus_schedules (id, trip_id, route_id, route_name, trip_direction, stop_id, stop_name, stop_sequence, arrival_time, departure_time, day_type) VALUES
(22, 403, 'R1', '中央線', 'INBOUND', 'STOP_OFFICE', 'オフィス前', 1, '22:00:00', '22:00:00', 'WEEKDAY'),
(23, 403, 'R1', '中央線', 'INBOUND', 'STOP_MAIN', 'メイン通り', 2, '22:30:00', '22:30:00', 'WEEKDAY'),
(24, 403, 'R1', '中央線', 'INBOUND', 'STOP_HOME', '自宅前', 3, '23:00:00', '23:00:00', 'WEEKDAY');
