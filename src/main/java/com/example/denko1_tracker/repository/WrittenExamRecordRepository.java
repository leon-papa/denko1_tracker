package com.example.denko1_tracker.repository;

import com.example.denko1_tracker.entity.WrittenExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WrittenExamRecordRepository extends JpaRepository<WrittenExamRecord, Long> {
    List<WrittenExamRecord> findByUserIdOrderByExamYearAscAttemptNumberAsc(Long userId);
}
