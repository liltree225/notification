-- ============================================================
-- ИНИЦИАЛИЗАЦИЯ БАЗЫ notification
-- ============================================================

-- Создаём схему (если нужно)
CREATE SCHEMA IF NOT EXISTS notification_schema;

-- Переключаемся на схему
SET search_path TO notification_schema;

-- ============================================================
-- ТАБЛИЦА УВЕДОМЛЕНИЙ
-- ============================================================
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    order_id BIGINT NOT NULL,
    type VARCHAR(30) NOT NULL CHECK (type IN ('ORDER_CREATED', 'ORDER_PAID', 'ORDER_SHIPPED', 'ORDER_DELIVERED', 'ORDER_CANCELLED')),
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    channel VARCHAR(20) NOT NULL CHECK (channel IN ('EMAIL', 'SMS', 'PUSH', 'TELEGRAM')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'SENT', 'FAILED')),
    sent_at TIMESTAMP,
    retry_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ТАБЛИЦА НАСТРОЕК ПОЛЬЗОВАТЕЛЕЙ
-- ============================================================
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    email_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    sms_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    push_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    telegram_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    telegram_chat_id VARCHAR(255),
    preferred_channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL' CHECK (preferred_channel IN ('EMAIL', 'SMS', 'PUSH', 'TELEGRAM')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ТАБЛИЦА ШАБЛОНОВ УВЕДОМЛЕНИЙ
-- ============================================================
CREATE TABLE IF NOT EXISTS notification_templates (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(30) NOT NULL CHECK (type IN ('ORDER_CREATED', 'ORDER_PAID', 'ORDER_SHIPPED', 'ORDER_DELIVERED', 'ORDER_CANCELLED')),
    channel VARCHAR(20) NOT NULL CHECK (channel IN ('EMAIL', 'SMS', 'PUSH', 'TELEGRAM')),
    subject_template VARCHAR(255) NOT NULL,
    body_template TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (type, channel)
);

-- ============================================================
-- ИНДЕКСЫ
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_order_id ON notifications(order_id);
CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(type);
CREATE INDEX IF NOT EXISTS idx_notifications_status ON notifications(status);
CREATE INDEX IF NOT EXISTS idx_notifications_created_at ON notifications(created_at);
CREATE INDEX IF NOT EXISTS idx_user_preferences_user_id ON user_preferences(user_id);