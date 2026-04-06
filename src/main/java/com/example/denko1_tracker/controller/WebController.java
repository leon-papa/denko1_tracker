package com.example.denko1_tracker.controller;

import com.example.denko1_tracker.entity.SkillExamRecord;
import com.example.denko1_tracker.entity.WrittenExamRecord;
import com.example.denko1_tracker.entity.User;
import com.example.denko1_tracker.repository.SkillExamRecordRepository;
import com.example.denko1_tracker.repository.WrittenExamRecordRepository;
import com.example.denko1_tracker.repository.UserRepository;
import com.example.denko1_tracker.service.WeaknessAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        Long userId = user.getId();

        List<WrittenExamRecord> writtenRecords = writtenExamRecordRepository.findByUserIdOrderByExamYearAscAttemptNumberAsc(userId);
        List<SkillExamRecord> skillRecords = skillExamRecordRepository.findByUserIdOrderByProblemNumberAscAttemptNumberAsc(userId);

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
        record.setUserId(user.getId());
        writtenExamRecordRepository.save(record);
        return "redirect:/";
    }

    @PostMapping("/record/skill")
    public String saveSkill(@AuthenticationPrincipal UserDetails userDetails, SkillExamRecord record) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        record.setUserId(user.getId());
        skillExamRecordRepository.save(record);
        return "redirect:/";
    }
}
