package com.example.denko1_tracker.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WrittenExamRecordTest {

    @Test
    public void testGetTotalScore() {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setCalcScore(10);
        record.setMemoryScore(20);
        record.setDiagramScore(30);
        record.setLawScore(5);
        
        assertEquals(65, record.getTotalScore());
    }

    @Test
    public void testGetTotalScoreWithNulls() {
        WrittenExamRecord record = new WrittenExamRecord();
        record.setCalcScore(10);
        // memoryScore is null
        record.setDiagramScore(null);
        record.setLawScore(5);
        
        assertEquals(15, record.getTotalScore());
    }
}
