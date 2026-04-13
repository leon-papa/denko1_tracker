package com.example.denko1_tracker.repository;

import com.example.denko1_tracker.entity.WrittenExamRecord;
import com.example.denko1_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WrittenExamRecordRepository extends JpaRepository<WrittenExamRecord, Long> {
    List<WrittenExamRecord> findByUserOrderByExamYearAscAttemptNumberAsc(User user);
    List<WrittenExamRecord> findByUserOrderByUpdatedAtDesc(User user);
}
