-- ============================================
-- OHT/AGV 열화 예지보전 모니터링 시스템
-- schema.sql (erdcloud Export 수정본)
-- ============================================

-- DROP (자식 테이블부터 삭제)
DROP TABLE IF EXISTS `sensor_reading`;
DROP TABLE IF EXISTS `maint_order`;
DROP TABLE IF EXISTS `alert_event`;
DROP TABLE IF EXISTS `equipment`;
DROP TABLE IF EXISTS `production_line`;
DROP TABLE IF EXISTS `fab`;
DROP TABLE IF EXISTS `threshold_rule`;
DROP TABLE IF EXISTS `member`;

-- ============================================
-- 1. 공장
-- ============================================
CREATE TABLE `fab` (
	`fab_id`       VARCHAR(10)   NOT NULL  COMMENT 'PK, FAB-A/FAB-B',
	`fab_name`     VARCHAR(50)   NOT NULL  COMMENT '공장명',
	`created_at`   TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '생성일시',
	PRIMARY KEY (`fab_id`)
);

-- ============================================
-- 2. 생산라인
-- ============================================
CREATE TABLE `production_line` (
	`line_id`      VARCHAR(10)   NOT NULL  COMMENT 'PK, LINE-1~4',
	`fab_id`       VARCHAR(10)   NOT NULL  COMMENT 'FK → fab',
	`line_name`    VARCHAR(50)   NOT NULL  COMMENT '라인명',
	`eq_type`      ENUM('OHT','AGV')  NOT NULL  COMMENT '장비 유형',
	`total_slots`  INT           NOT NULL  DEFAULT 10   COMMENT '정원',
	`min_running`  INT           NOT NULL  DEFAULT 7    COMMENT '최소 가동 수',
	`created_at`   TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '생성일시',
	PRIMARY KEY (`line_id`),
	FOREIGN KEY (`fab_id`) REFERENCES `fab` (`fab_id`)
);

-- ============================================
-- 3. 장비
-- ============================================
CREATE TABLE `equipment` (
	`eq_id`         VARCHAR(20)   NOT NULL  COMMENT 'PK, OHT-01/AGV-01',
	`eq_name`       VARCHAR(100)  NOT NULL  COMMENT '장비명',
	`eq_type`       ENUM('OHT','AGV')  NOT NULL  COMMENT '장비 유형',
	`fab_id`        VARCHAR(10)   NOT NULL  COMMENT 'FK → fab',
	`line_id`       VARCHAR(10)   NOT NULL  COMMENT 'FK → production_line',
	`manufacturer`  VARCHAR(50)   NULL      COMMENT '제조사',
	`installer`     VARCHAR(50)   NULL      COMMENT '설치 작업자',
	`location`      VARCHAR(100)  NULL      COMMENT '설치 위치',
	`install_date`  DATE          NULL      COMMENT '설치일',
	`status`        ENUM('RUNNING','STOPPED','MAINTENANCE')  NOT NULL  DEFAULT 'RUNNING'  COMMENT '장비 상태',
	`created_at`    TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '생성일시',
	PRIMARY KEY (`eq_id`),
	FOREIGN KEY (`fab_id`) REFERENCES `fab` (`fab_id`),
	FOREIGN KEY (`line_id`) REFERENCES `production_line` (`line_id`)
);

-- ============================================
-- 4. 센서 데이터
-- ============================================
CREATE TABLE `sensor_reading` (
	`sensor_reading_id`       BIGINT        NOT NULL  AUTO_INCREMENT  COMMENT 'PK',
	`eq_id`                   VARCHAR(20)   NOT NULL  COMMENT 'FK → equipment',
	`collected_at`            TIMESTAMP     NOT NULL  COMMENT '수집 시각',
	`pm10`                    DOUBLE        NULL      COMMENT '미세먼지 PM10',
	`pm25`                    DOUBLE        NULL      COMMENT '미세먼지 PM2.5',
	`pm10_val`                DOUBLE        NULL      COMMENT 'PM10 보정값',
	`ntc_temp`                DOUBLE        NULL      COMMENT '내부온도',
	`ct1`                     DOUBLE        NULL      COMMENT '전류 채널1',
	`ct2`                     DOUBLE        NULL      COMMENT '전류 채널2',
	`ct3`                     DOUBLE        NULL      COMMENT '전류 채널3',
	`ct4`                     DOUBLE        NULL      COMMENT '전류 채널4',
	`ir_temp_max`             DOUBLE        NULL      COMMENT '열화상 최고온도',
	`ir_x`                    INT           NULL      COMMENT '최고온도 X좌표',
	`ir_y`                    INT           NULL      COMMENT '최고온도 Y좌표',
	`ex_temp`                 DOUBLE        NULL      COMMENT '외부온도',
	`ex_humidity`             DOUBLE        NULL      COMMENT '외부습도',
	`ex_lux`                  DOUBLE        NULL      COMMENT '외부조도',
	`state`                   TINYINT       NULL      COMMENT '0정상/1관심/2경고/3위험',
	`cumulative_operating_day` INT          NULL      COMMENT '누적 가동일',
	`equipment_history`       INT           NULL      COMMENT '장비 이력 횟수',
	`ir_image_path`           VARCHAR(255)  NULL      COMMENT '열화상 BIN 경로',
	PRIMARY KEY (`sensor_reading_id`),
	FOREIGN KEY (`eq_id`) REFERENCES `equipment` (`eq_id`),
	INDEX `idx_eq_collected` (`eq_id`, `collected_at`)
);

-- ============================================
-- 5. 알림 이벤트
-- ============================================
CREATE TABLE `alert_event` (
	`alert_id`      BIGINT        NOT NULL  AUTO_INCREMENT  COMMENT 'PK',
	`eq_id`         VARCHAR(20)   NOT NULL  COMMENT 'FK → equipment',
	`alert_level`   TINYINT       NOT NULL  COMMENT '1관심/2경고/3위험',
	`sensor_name`   VARCHAR(20)   NOT NULL  COMMENT '센서명',
	`sensor_value`  DOUBLE        NOT NULL  COMMENT '실제 센서값',
	`threshold`     DOUBLE        NOT NULL  COMMENT '기준 임계값',
	`acknowledged`  BOOLEAN       NOT NULL  DEFAULT FALSE  COMMENT '확인 여부',
	`ack_at`        TIMESTAMP     NULL      COMMENT '확인 시각',
	`created_at`    TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '발생 시각',
	PRIMARY KEY (`alert_id`),
	FOREIGN KEY (`eq_id`) REFERENCES `equipment` (`eq_id`)
);

-- ============================================
-- 6. 회원
-- ============================================
CREATE TABLE `member` (
	`login_id`       VARCHAR(20)   NOT NULL  COMMENT 'PK, 로그인 아이디',
	`password`       VARCHAR(60)   NOT NULL  COMMENT 'BCrypt 해시',
	`member_name`    VARCHAR(50)   NOT NULL  COMMENT '이름',
	`employee_code`  VARCHAR(20)   NOT NULL  COMMENT '사원코드',
	`email`          VARCHAR(100)  NOT NULL  COMMENT '이메일',
	`role`           ENUM('ADMIN','USER')  NOT NULL  DEFAULT 'USER'  COMMENT 'ADMIN/USER',
	`created_at`     TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '가입일시',
	PRIMARY KEY (`login_id`),
	UNIQUE KEY (`employee_code`)
);

-- ============================================
-- 7. 정비 오더
-- ============================================
CREATE TABLE `maint_order` (
	`order_id`        BIGINT        NOT NULL  AUTO_INCREMENT  COMMENT 'PK',
	`eq_id`           VARCHAR(20)   NOT NULL  COMMENT 'FK → equipment',
	`alert_id`        BIGINT        NULL      COMMENT 'FK → alert_event (수동 생성 시 NULL)',
	`assignee_id`     VARCHAR(20)   NULL      COMMENT 'FK → member (담당자)',
	`order_type`      ENUM('EMERGENCY','CORRECTIVE')  NOT NULL  COMMENT '오더 유형',
	`priority`        ENUM('HIGH','MEDIUM','LOW')      NOT NULL  DEFAULT 'MEDIUM'  COMMENT '우선순위',
	`status`          ENUM('OPEN','ASSIGNED','IN_PROGRESS','COMPLETED')  NOT NULL  DEFAULT 'OPEN'  COMMENT '오더 상태',
	`title`           VARCHAR(200)  NOT NULL  COMMENT '제목',
	`description`     TEXT          NULL      COMMENT '설명',
	`resolution`      TEXT          NULL      COMMENT '조치 내용',
	`completed_at`    TIMESTAMP     NULL      COMMENT '완료 시각',
	`created_at`      TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '생성 시각',
	PRIMARY KEY (`order_id`),
	FOREIGN KEY (`eq_id`) REFERENCES `equipment` (`eq_id`),
	FOREIGN KEY (`alert_id`) REFERENCES `alert_event` (`alert_id`),
	FOREIGN KEY (`assignee_id`) REFERENCES `member` (`login_id`)
);

-- ============================================
-- 8. 임계값 규칙
-- ============================================
CREATE TABLE `threshold_rule` (
	`rule_id`       BIGINT        NOT NULL  AUTO_INCREMENT  COMMENT 'PK',
	`eq_type`       ENUM('OHT','AGV')  NOT NULL  COMMENT '장비 유형',
	`sensor_name`   VARCHAR(20)   NOT NULL  COMMENT '센서명',
	`warn_level1`   DOUBLE        NOT NULL  COMMENT '관심 임계값',
	`warn_level2`   DOUBLE        NOT NULL  COMMENT '경고 임계값',
	`warn_level3`   DOUBLE        NOT NULL  COMMENT '위험 임계값',
	`updated_at`    TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '수정 시각',
	PRIMARY KEY (`rule_id`)
);
