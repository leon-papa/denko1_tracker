package com.example.denko1_tracker.controller;

import com.example.denko1_tracker.entity.SkillExamRecord;
import com.example.denko1_tracker.entity.WrittenExamRecord;
import com.example.denko1_tracker.entity.User;
import com.example.denko1_tracker.repository.SkillExamRecordRepository;
import com.example.denko1_tracker.repository.WrittenExamRecordRepository;
import com.example.denko1_tracker.repository.UserRepository;
import com.example.denko1_tracker.service.WeaknessAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private WrittenExamRecordRepository writtenExamRecordRepository;
    
    @Autowired
    private SkillExamRecordRepository skillExamRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeaknessAnalysisService analysisService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        List<WrittenExamRecord> writtenRecords = writtenExamRecordRepository.findByUserOrderByExamYearAscAttemptNumberAsc(user);
        List<SkillExamRecord> skillRecords = skillExamRecordRepository.findByUserOrderByProblemNumberAscAttemptNumberAsc(user);

        if (!writtenRecords.isEmpty()) {
            WrittenExamRecord latestRecord = writtenRecords.get(writtenRecords.size() - 1);
            String weakest = analysisService.findWeakestCategory(latestRecord);
            Map<String, Double> percentages = analysisService.calculateAnalysis(latestRecord);
            
            model.addAttribute("latestRecord", latestRecord);
            model.addAttribute("weakestCategory", weakest);
            model.addAttribute("chartData", percentages.values()); 
            model.addAttribute("chartLabels", percentages.keySet());
        } else {
            model.addAttribute("weakestCategory", "データがありません");
        }

        model.addAttribute("writtenRecords", writtenRecords);
        model.addAttribute("skillRecords", skillRecords);
        model.addAttribute("username", user.getUsername());
        
        return "index";
    }

    @GetMapping("/record")
    public String showRecordForm(Model model) {
        model.addAttribute("writtenRecord", new WrittenExamRecord());
        model.addAttribute("skillRecord", new SkillExamRecord());
        return "record";
    }

    @PostMapping("/record/written")
    public String saveWritten(@AuthenticationPrincipal UserDetails userDetails, WrittenExamRecord record) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        record.setUser(user);
        writtenExamRecordRepository.save(record);
        return "redirect:/";
    }

    @PostMapping("/record/skill")
    public String saveSkill(@ModelAttribute SkillExamRecord record, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        record.setUser(user);
        skillExamRecordRepository.save(record);
        return "redirect:/";
    }

    @PostMapping("/written/delete/{id}")
    public String deleteWrittenRecord(@PathVariable Long id, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        writtenExamRecordRepository.findById(id).ifPresent(record -> {
            if (record.getUser().getId().equals(user.getId())) {
                writtenExamRecordRepository.delete(record);
            }
        });
        return "redirect:/";
    }

    @PostMapping("/skill/delete/{id}")
    public String deleteSkillRecord(@PathVariable Long id, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        skillExamRecordRepository.findById(id).ifPresent(record -> {
            if (record.getUser().getId().equals(user.getId())) {
                skillExamRecordRepository.delete(record);
            }
        });
        return "redirect:/";
    }

    @GetMapping("/download/written")
    public ResponseEntity<byte[]> downloadWrittenCsv(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<WrittenExamRecord> records = writtenExamRecordRepository.findByUserOrderByExamYearAscAttemptNumberAsc(user);

        StringBuilder sb = new StringBuilder();
        // UTF-8 BOM for Excel
        sb.append("\uFEFF");
        sb.append("年度,回数,計算点,暗記点,図面点,法令点\n");
        for (WrittenExamRecord r : records) {
            sb.append(String.format("%s,%d,%d,%d,%d,%d\n",
                r.getExamYear(), r.getAttemptNumber(), r.getCalcScore(), r.getMemoryScore(), r.getDiagramScore(), r.getLawScore()));
        }

        return createCsvResponse(sb.toString(), "written_exam_records.csv");
    }

    @GetMapping("/download/skill")
    public ResponseEntity<byte[]> downloadSkillCsv(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<SkillExamRecord> records = skillExamRecordRepository.findByUserOrderByProblemNumberAscAttemptNumberAsc(user);

        StringBuilder sb = new StringBuilder();
        sb.append("\uFEFF");
        sb.append("問題番号,回数,完成時間(分),合否\n");
        for (SkillExamRecord r : records) {
            sb.append(String.format("No.%d,%d,%d,%s\n",
                r.getProblemNumber(), r.getAttemptNumber(), r.getCompletionTimeMinutes(), r.getIsPassed() ? "合格" : "欠陥あり"));
        }

        return createCsvResponse(sb.toString(), "skill_exam_records.csv");
    }

    private ResponseEntity<byte[]> createCsvResponse(String content, String filename) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(data);
    }
}
