-- Datos de prueba para PowerSense
-- Usuario: test@example.com / Password: password123 (BCrypt hash)
-- INTO users (id, email, password_hash, name, avatar_url, is_active, created_at, updated_at)
-- VALUES (1, 'test@example.com', '$2a$10$8K1p/a0dx9S3n4.3Z.W.euY/b6tYvG1.z9A3Z4Z3Z4Z3Z4Z3Z4Z3Z', 'Usuario Prueba', '', 1, NOW(), NOW());

-- Dispositivos para el usuario 1
INSERT INTO devices (id, name, category, status, room_id, room_name, watts, user_id, created_at, updated_at)
VALUES ('dev-001', 'Televisor Sala', 'TV', 'ACTIVE', 'room-1', 'Sala', 150, 1, NOW(), NOW());

INSERT INTO devices (id, name, category, status, room_id, room_name, watts, user_id, created_at, updated_at)
VALUES ('dev-002', 'Refrigeradora', 'REFRIGERATOR', 'ACTIVE', 'room-2', 'Cocina', 350, 1, NOW(), NOW());

INSERT INTO devices (id, name, category, status, room_id, room_name, watts, user_id, created_at, updated_at)
VALUES ('dev-003', 'Aire Acondicionado', 'AC', 'INACTIVE', 'room-3', 'Dormitorio', 1200, 1, NOW(), NOW());


-- Alertas para el usuario 1
INSERT INTO alerts (id, type, severity, device_id, threshold, message, acknowledged, user_id, created_at, updated_at)
VALUES ('alt-001', 'HIGH_CONSUMPTION', 'CRITICAL', 'dev-003', 1000.0, 'El aire acondicionado está excediendo el límite de consumo.', 0, 1, NOW(), NOW());

INSERT INTO alerts (id, type, severity, device_id, threshold, message, acknowledged, user_id, created_at, updated_at)
VALUES ('alt-002', 'THRESHOLD_EXCEEDED', 'WARNING', 'dev-002', 400.0, 'Actividad inusual detectada en la refrigeradora.', 0, 1, NOW(), NOW());



-- Horarios para el usuario 1
INSERT INTO schedules (id, device_id, device_name, room_name, enabled, user_id, created_at, updated_at)
VALUES ('sch-001', 'dev-001', 'Televisor Sala', 'Sala', 1, 1, NOW(), NOW());

-- Entradas del horario (Encendido a las 18:00, Apagado a las 22:00)
INSERT INTO schedule_entries (id, schedule_id, action, hour, minute) VALUES (1, 'sch-001', 'ON', 18, 0);
INSERT INTO schedule_entries (id, schedule_id, action, hour, minute) VALUES (2, 'sch-001', 'OFF', 22, 0);

-- Días para las entradas del horario
INSERT INTO schedule_entry_days (entry_id, day) VALUES (1, 'MONDAY'), (1, 'TUESDAY'), (1, 'WEDNESDAY'), (1, 'THURSDAY'), (1, 'FRIDAY');
INSERT INTO schedule_entry_days (entry_id, day) VALUES (2, 'MONDAY'), (2, 'TUESDAY'), (2, 'WEDNESDAY'), (2, 'THURSDAY'), (2, 'FRIDAY');
