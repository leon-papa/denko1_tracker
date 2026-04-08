package com.example.denko1_tracker.repository;

import com.example.denko1_tracker.entity.SkillExamRecord;
import com.example.denko1_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillExamRecordRepository extends JpaRepository<SkillExamRecord, Long> {
    List<SkillExamRecord> findByUserOrderByProblemNumberAscAttemptNumberAsc(User user);
}
