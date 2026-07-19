package org.example.repository;

import org.example.domain.NotificationChannelResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationChannelResultsDao extends JpaRepository<NotificationChannelResults,Long> {
}
