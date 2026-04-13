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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        List<WrittenExamRecord> writtenRecords = writtenExamRecordRepository.findByUserOrderByExamYearAscAttemptNumberAsc(user);
        List<SkillExamRecord> skillRecords = skillExamRecordRepository.findByUserOrderByProblemNumberAscAttemptNumberAsc(user);

        if (!writtenRecords.isEmpty()) {
            // グラフ用には「最後に入力・更新した」レコードを取得
            List<WrittenExamRecord> latestUpdated = writtenExamRecordRepository.findByUserOrderByUpdatedAtDesc(user);
            WrittenExamRecord latestRecord = latestUpdated.isEmpty() ? writtenRecords.get(writtenRecords.size() - 1) : latestUpdated.get(0);
            
            String weakest = analysisService.findWeakestCategory(latestRecord);
            Map<String, Double> percentages = analysisService.calculateAnalysis(latestRecord);
            
            model.addAttribute("latestRecord", latestRecord);
            model.addAttribute("weakestCategory", weakest);
            model.addAttribute("chartData", new ArrayList<>(percentages.values())); 
            model.addAttribute("chartLabels", new ArrayList<>(percentages.keySet()));
        } else {
            model.addAttribute("weakestCategory", "データがありません");
        }

        model.addAttribute("writtenRecords", writtenRecords);
        model.addAttribute("skillRecords", skillRecords);
        model.addAttribute("username", user.getUsername());
        
        // カウントダウン計算
        LocalDate today = LocalDate.now();
        if (user.getWrittenExamDate() != null) {
            long days = today.until(user.getWrittenExamDate(), ChronoUnit.DAYS);
            model.addAttribute("daysToWritten", days);
            model.addAttribute("writtenDate", user.getWrittenExamDate());
        }
        if (user.getSkillExamDate() != null) {
            long days = today.until(user.getSkillExamDate(), ChronoUnit.DAYS);
            model.addAttribute("daysToSkill", days);
            model.addAttribute("skillDate", user.getSkillExamDate());
        }

        return "index";
    }

    @GetMapping("/record")
    public String showRecordForm(Model model) {
        model.addAttribute("writtenRecord", new WrittenExamRecord());
        model.addAttribute("skillRecord", new SkillExamRecord());
        return "record";
    }

    @GetMapping("/written/edit/{id}")
    public String showEditWrittenForm(@PathVariable Long id, Authentication auth, Model model) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        WrittenExamRecord record = writtenExamRecordRepository.findById(id)
            .filter(r -> r.getUser().getId().equals(user.getId()))
            .orElseThrow();
        model.addAttribute("writtenRecord", record);
        model.addAttribute("skillRecord", new SkillExamRecord());
        model.addAttribute("editMode", "written");
        return "record";
    }

    @GetMapping("/skill/edit/{id}")
    public String showEditSkillForm(@PathVariable Long id, Authentication auth, Model model) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        SkillExamRecord record = skillExamRecordRepository.findById(id)
            .filter(r -> r.getUser().getId().equals(user.getId()))
            .orElseThrow();
        model.addAttribute("writtenRecord", new WrittenExamRecord());
        model.addAttribute("skillRecord", record);
        model.addAttribute("editMode", "skill");
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
        sb.append("年度,回数,計算,機器,施工図,法令\n");
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

    // ===== ユーザー設定 =====

    @GetMapping("/settings")
    public String showSettings(Authentication auth, Model model) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("writtenExamDate", user.getWrittenExamDate());
        model.addAttribute("skillExamDate", user.getSkillExamDate());
        return "settings";
    }


    @PostMapping("/settings/exam-dates")
    public String updateExamDates(@RequestParam(required = false) String writtenExamDate,
                                  @RequestParam(required = false) String skillExamDate,
                                  Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        
        if (writtenExamDate != null && !writtenExamDate.isEmpty()) {
            user.setWrittenExamDate(LocalDate.parse(writtenExamDate));
        } else {
            user.setWrittenExamDate(null);
        }
        
        if (skillExamDate != null && !skillExamDate.isEmpty()) {
            user.setSkillExamDate(LocalDate.parse(skillExamDate));
        } else {
            user.setSkillExamDate(null);
        }
        
        userRepository.save(user);
        return "redirect:/settings?datesUpdated";
    }


    @PostMapping("/settings/password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Authentication auth, Model model) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "redirect:/settings?passwordError";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "redirect:/settings?passwordUpdated";
    }

    @PostMapping("/settings/delete-account")
    public String deleteAccount(Authentication auth,
                                jakarta.servlet.http.HttpServletRequest request) throws Exception {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        userRepository.delete(user);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        return "redirect:/login?accountDeleted";
    }
}
