package com.example.denko1_tracker.service;

import com.example.denko1_tracker.entity.WrittenExamRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeaknessAnalysisServiceTest {

    private WeaknessAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new WeaknessAnalysisService();
    }

    @Test
    @DisplayName("全てのカテゴリで正答率が正しく計算されること")
    void testCalculateAnalysis() {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setCalcScore(10);   // 10/20 = 50.0%
        record.setMemoryScore(15); // 15/30 = 50.0%
        record.setDiagramScore(24); // 24/30 = 80.0%
        record.setLawScore(10);    // 10/20 = 50.0%

        Map<String, Double> result = service.calculateAnalysis(record);

        assertEquals(50.0, result.get("計算問題"));
        assertEquals(50.0, result.get("記憶問題"));
        assertEquals(80.0, result.get("図面・回路"));
        assertEquals(50.0, result.get("法令等"));
    }

    @Test
    @DisplayName("最も低い正答率のカテゴリが正しく特定されること")
    void testFindWeakestCategory() {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setCalcScore(16);   // 80%
        record.setMemoryScore(20); // 66.7%
        record.setDiagramScore(15); // 50.0% (Weakest)
        record.setLawScore(18);    // 90%

        String weakest = service.findWeakestCategory(record);

        assertEquals("図面・回路", weakest);
    }

    @Test
    @DisplayName("スコアが0点の場合でも正しく0%と計算されること")
    void testZeroScore() {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setCalcScore(0);
        record.setMemoryScore(0);
        record.setDiagramScore(0);
        record.setLawScore(0);

        Map<String, Double> result = service.calculateAnalysis(record);

        assertEquals(0.0, result.get("計算問題"));
        assertEquals("計算問題", service.findWeakestCategory(record)); // 最初に見つかった最小値
    }
}
