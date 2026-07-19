package org.example.repository;

import org.example.domain.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferencesDao extends JpaRepository<UserPreferences,Long> {
    Optional<UserPreferences> findByUserId(Long id);
}
