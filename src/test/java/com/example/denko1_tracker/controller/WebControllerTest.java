package com.example.denko1_tracker.controller;

import com.example.denko1_tracker.entity.User;
import com.example.denko1_tracker.entity.WrittenExamRecord;
import com.example.denko1_tracker.repository.WrittenExamRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class WebControllerTest {

    @Mock
    private WrittenExamRecordRepository writtenExamRecordRepository;

    @InjectMocks
    private WebController webController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("筆記試験の履歴データが年度(降順)・期(上・下・前・後)・回数(昇順)で正しくソートされること")
    void testGetSortedWrittenRecords() {
        User user = new User();
        user.setId(1L);

        // テスト用データの作成
        WrittenExamRecord r1 = createRecord(2024, "上期", 2);
        WrittenExamRecord r2 = createRecord(2025, "上期", 1);
        WrittenExamRecord r3 = createRecord(2025, "下期", 1);
        WrittenExamRecord r4 = createRecord(2025, "上期", 2);
        WrittenExamRecord r5 = createRecord(2024, "下期", 1);
        WrittenExamRecord r6 = createRecord(2025, "午前", 1);
        WrittenExamRecord r7 = createRecord(2025, "午後", 1);

        List<WrittenExamRecord> mockRecords = Arrays.asList(r1, r2, r3, r4, r5, r6, r7);
        when(writtenExamRecordRepository.findByUser(user)).thenReturn(mockRecords);

        // テスト実行
        List<WrittenExamRecord> sortedRecords = webController.getSortedWrittenRecords(user);

        // 検証
        assertEquals(7, sortedRecords.size());
        
        // 1. 2025年度 上期 1回目
        assertEquals(2025, sortedRecords.get(0).getExamYear());
        assertEquals("上期", sortedRecords.get(0).getExamPeriod());
        assertEquals(1, sortedRecords.get(0).getAttemptNumber());

        // 2. 2025年度 上期 2回目
        assertEquals(2025, sortedRecords.get(1).getExamYear());
        assertEquals("上期", sortedRecords.get(1).getExamPeriod());
        assertEquals(2, sortedRecords.get(1).getAttemptNumber());

        // 3. 2025年度 下期 1回目
        assertEquals(2025, sortedRecords.get(2).getExamYear());
        assertEquals("下期", sortedRecords.get(2).getExamPeriod());
        assertEquals(1, sortedRecords.get(2).getAttemptNumber());

        // 4. 2025年度 午前 1回目
        assertEquals(2025, sortedRecords.get(3).getExamYear());
        assertEquals("午前", sortedRecords.get(3).getExamPeriod());
        assertEquals(1, sortedRecords.get(3).getAttemptNumber());

        // 5. 2025年度 午後 1回目
        assertEquals(2025, sortedRecords.get(4).getExamYear());
        assertEquals("午後", sortedRecords.get(4).getExamPeriod());
        assertEquals(1, sortedRecords.get(4).getAttemptNumber());

        // 6. 2024年度 上期 2回目
        assertEquals(2024, sortedRecords.get(5).getExamYear());
        assertEquals("上期", sortedRecords.get(5).getExamPeriod());
        assertEquals(2, sortedRecords.get(5).getAttemptNumber());

        // 7. 2024年度 下期 1回目
        assertEquals(2024, sortedRecords.get(6).getExamYear());
        assertEquals("下期", sortedRecords.get(6).getExamPeriod());
        assertEquals(1, sortedRecords.get(6).getAttemptNumber());
    }

    private WrittenExamRecord createRecord(int year, String period, int attempt) {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setExamYear(year);
        record.setExamPeriod(period);
        record.setAttemptNumber(attempt);
        return record;
    }
}
