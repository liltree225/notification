package org.example.repository;

import lombok.NonNull;
import org.example.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NotificationDao extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserIdAndType(Long userId, String eventType, Pageable pageable);
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    Optional<Notification> findById(@NonNull Long userId);
}
