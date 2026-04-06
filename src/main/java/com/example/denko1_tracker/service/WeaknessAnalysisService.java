package com.example.denko1_tracker.service;

import com.example.denko1_tracker.entity.WrittenExamRecord;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 筆記試験の入力を分析し、弱点を特定するサービス。
 * 配点（満点）は将来的な変更にも対応できるよう定数として定義しています。
 */
@Service
public class WeaknessAnalysisService {

    // 配点設定（満点）
    public static final int MAX_CALC_SCORE = 20;
    public static final int MAX_MEMORY_SCORE = 30;
    public static final int MAX_DIAGRAM_SCORE = 30;
    public static final int MAX_LAW_SCORE = 20;

    /**
     * 各カテゴリの正答率（%）を計算します。
     */
    public Map<String, Double> calculateAnalysis(WrittenExamRecord record) {
        Map<String, Double> analysis = new LinkedHashMap<>();
        
        analysis.put("計算問題", calculatePercentage(record.getCalcScore(), MAX_CALC_SCORE));
        analysis.put("記憶問題", calculatePercentage(record.getMemoryScore(), MAX_MEMORY_SCORE));
        analysis.put("図面・回路", calculatePercentage(record.getDiagramScore(), MAX_DIAGRAM_SCORE));
        analysis.put("法令等", calculatePercentage(record.getLawScore(), MAX_LAW_SCORE));
        
        return analysis;
    }

    /**
     * 正答率が最も低いカテゴリ名を特定します。
     */
    public String findWeakestCategory(WrittenExamRecord record) {
        Map<String, Double> analysis = calculateAnalysis(record);
        
        String weakest = "";
        double minPercentage = 101.0; // 100%を超える値で初期化

        for (Map.Entry<String, Double> entry : analysis.entrySet()) {
            if (entry.getValue() < minPercentage) {
                minPercentage = entry.getValue();
                weakest = entry.getKey();
            }
        }
        
        return weakest;
    }

    private double calculatePercentage(Integer score, int max) {
        if (score == null || max == 0) return 0.0;
        double percentage = (double) score / max * 100;
        return Math.round(percentage * 10.0) / 10.0; // 小数第1位で四捨五入
    }
}
