package com.example.denko1_tracker;

import com.example.denko1_tracker.entity.User;
import com.example.denko1_tracker.entity.WrittenExamRecord;
import com.example.denko1_tracker.entity.SkillExamRecord;
import com.example.denko1_tracker.repository.UserRepository;
import com.example.denko1_tracker.repository.WrittenExamRecordRepository;
import com.example.denko1_tracker.repository.SkillExamRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WrittenExamRecordRepository writtenExamRecordRepository;

    @Autowired
    private SkillExamRecordRepository skillExamRecordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // H2などのインメモリDBを利用したローカル起動時にテストデータを投入
        if (userRepository.count() == 0) {
            System.out.println("====== [Local Test Data Setup] Inserting initial records ======");

            // 1. テストユーザーの作成
            User testUser = new User();
            testUser.setUsername("user");
            testUser.setPassword(passwordEncoder.encode("password"));
            testUser.setShowWrittenResults(true);
            testUser.setWrittenExamDate(LocalDate.now().plusDays(30)); // 30日後
            testUser.setSkillExamDate(LocalDate.now().plusDays(60));   // 60日後
            userRepository.save(testUser);

            // 2. 筆記試験レコードの挿入
            // ソート順の動作確認がしやすいよう、バラバラの順序で保存します
            saveWrittenRecord(testUser, 2024, "上期", 2, 10, 15, 20, 8, 1);
            saveWrittenRecord(testUser, 2025, "上期", 1, 12, 10, 25, 9, 2);
            saveWrittenRecord(testUser, 2025, "下期", 1, 14, 12, 30, 10, 3);
            saveWrittenRecord(testUser, 2025, "上期", 2, 18, 16, 40, 9, 4); // 最新更新データ
            saveWrittenRecord(testUser, 2024, "下期", 1, 10, 11, 15, 7, 5);
            saveWrittenRecord(testUser, 2025, "午前", 1, 12, 14, 28, 8, 6);
            saveWrittenRecord(testUser, 2025, "午後", 1, 15, 15, 35, 9, 7);

            // 3. 技能試験レコードの挿入
            saveSkillRecord(testUser, 1, 1, 55, true);
            saveSkillRecord(testUser, 1, 2, 45, true);
            saveSkillRecord(testUser, 2, 1, 65, false);

            System.out.println("====== [Local Test Data Setup] Successfully inserted records ======");
        }
    }

    private void saveWrittenRecord(User user, int year, String period, int attempt, int calc, int memory, int diagram, int law, int offsetSeconds) {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setUser(user);
        record.setExamYear(year);
        record.setExamPeriod(period);
        record.setAttemptNumber(attempt);
        record.setCalcScore(calc);
        record.setMemoryScore(memory);
        record.setDiagramScore(diagram);
        record.setLawScore(law);
        // updatedAt を少しずらして最新データを確定しやすくする
        record.setUpdatedAt(LocalDateTime.now().plusSeconds(offsetSeconds));
        writtenExamRecordRepository.save(record);
    }

    private void saveSkillRecord(User user, int problemNum, int attempt, int time, boolean isPassed) {
        SkillExamRecord record = new SkillExamRecord();
        record.setUser(user);
        record.setProblemNumber(problemNum);
        record.setAttemptNumber(attempt);
        record.setCompletionTimeMinutes(time);
        record.setIsPassed(isPassed);
        skillExamRecordRepository.save(record);
    }
}
