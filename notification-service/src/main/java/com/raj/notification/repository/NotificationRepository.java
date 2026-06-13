package com.raj.notification.repository;

import com.raj.notification.entity.NotificationEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<NotificationEventLog, UUID> {
}