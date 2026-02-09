package com.pictet.adventurebook.repository;

import com.pictet.adventurebook.domain.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
}
