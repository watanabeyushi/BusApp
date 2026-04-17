-- Sample timetable: route R1 at stop STOP_MAIN (weekday + Saturday + ALL examples)
INSERT INTO bus_schedules (id, route_id, route_name, stop_id, departure_time, day_type) VALUES
(1, 'R1', '中央線', 'STOP_MAIN', '06:30:00', 'WEEKDAY'),
(2, 'R1', '中央線', 'STOP_MAIN', '07:00:00', 'WEEKDAY'),
(3, 'R1', '中央線', 'STOP_MAIN', '07:30:00', 'WEEKDAY'),
(4, 'R1', '中央線', 'STOP_MAIN', '08:00:00', 'WEEKDAY'),
(5, 'R1', '中央線', 'STOP_MAIN', '22:30:00', 'WEEKDAY'),
(6, 'R1', '中央線', 'STOP_MAIN', '09:00:00', 'SATURDAY'),
(7, 'R1', '中央線', 'STOP_MAIN', '10:00:00', 'ALL');
