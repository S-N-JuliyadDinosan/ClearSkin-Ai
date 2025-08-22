package com.clearskin_ai.userservice.repository;

import com.clearskin_ai.userservice.entity.AnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisHistoryRepository extends JpaRepository<AnalysisHistory, Long> {
    @Query("SELECT a FROM AnalysisHistory a WHERE a.userId = :userId")
    List<AnalysisHistory> findByUserId(Long userId);

    @Query("SELECT COUNT(*) FROM AnalysisHistory")
    long countAnalyses();

    @Query("SELECT a FROM AnalysisHistory a WHERE a.userId IS NULL")
    List<AnalysisHistory> findAnonymousAnalyses();
}