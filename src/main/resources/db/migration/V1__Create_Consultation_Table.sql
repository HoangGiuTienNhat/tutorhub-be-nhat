-- Create consultations table
CREATE TABLE IF NOT EXISTS consultations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    consultation_date DATE NOT NULL,
    consultation_time TIME NOT NULL,
    type VARCHAR(50) NOT NULL,
    location_link VARCHAR(500),
    status VARCHAR(50),
    group_id BIGINT NOT NULL,
    FOREIGN KEY (group_id) REFERENCES student_groups(id) ON DELETE CASCADE,
    INDEX idx_group_id (group_id),
    INDEX idx_consultation_date (consultation_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

