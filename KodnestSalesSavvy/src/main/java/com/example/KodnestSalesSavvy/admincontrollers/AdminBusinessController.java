package com.example.KodnestSalesSavvy.admincontrollers;

import com.example.KodnestSalesSavvy.adminservices.AdminBusinessServiceContract;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/admin/business")
public class AdminBusinessController {

    private final AdminBusinessServiceContract adminBusinessService;

    public AdminBusinessController(AdminBusinessServiceContract adminBusinessService) {
        this.adminBusinessService = adminBusinessService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyBusiness(@RequestParam int month, @RequestParam int year) {
        try {
            Map<String, Object> report = adminBusinessService.calculateMonthlyBusiness(month, year);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyBusiness(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Map<String, Object> report = adminBusinessService.calculateDailyBusiness(localDate);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/yearly")
    public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {
        try {
            Map<String, Object> report = adminBusinessService.calculateYearlyBusiness(year);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }    @GetMapping("/overall")
    public ResponseEntity<?> getOverallBusiness() {
        try {
            Map<String, Object> report = adminBusinessService.calculateOverallBusiness();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error calculating overall business");
        }
    }
}