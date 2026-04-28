package com.scholarship.platform.config;

import com.scholarship.platform.entity.FinancialAid;
import com.scholarship.platform.entity.Scholarship;
import com.scholarship.platform.entity.User;
import com.scholarship.platform.repository.FinancialAidRepository;
import com.scholarship.platform.repository.ScholarshipRepository;
import com.scholarship.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final FinancialAidRepository financialAidRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedScholarships();
        seedFinancialAids();
    }

    // ── Default Users ──────────────────────────────────────────────────────────

    private void seedUsers() {
        if (userRepository.findByEmail("admin@scholar.com").isEmpty()) {
            userRepository.save(User.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin@scholar.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .status(User.UserStatus.ACTIVE)
                .build());
            log.info("Seeded admin user: admin@scholar.com / admin123");
        }

        if (userRepository.findByEmail("student@scholar.com").isEmpty()) {
            userRepository.save(User.builder()
                .firstName("Student")
                .lastName("Demo")
                .email("student@scholar.com")
                .password(passwordEncoder.encode("student123"))
                .role(User.Role.STUDENT)
                .status(User.UserStatus.ACTIVE)
                .institution("State University")
                .major("Computer Science")
                .gpa(3.8)
                .build());
            log.info("Seeded student user: student@scholar.com / student123");
        }
    }

    // ── Scholarships ───────────────────────────────────────────────────────────

    private void seedScholarships() {
        if (scholarshipRepository.count() > 0) {
            log.info("Scholarships already seeded ({} records)", scholarshipRepository.count());
            return;
        }

        scholarshipRepository.save(Scholarship.builder()
            .title("National Merit Scholarship Program")
            .provider("National Merit Scholarship Corp")
            .category("STEM")
            .amount(new BigDecimal("8000"))
            .deadline(LocalDate.of(2026, 6, 30))
            .description("For academically talented students who demonstrate exceptional ability and potential for educational and professional achievement.")
            .eligibility("High school seniors with exceptional PSAT scores and academic record.")
            .gpaRequirement(3.5)
            .seats(100)
            .renewable(true)
            .renewalCriteria("Maintain 3.5 GPA each semester")
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("STEM Excellence Award")
            .provider("Tech Foundation USA")
            .category("STEM")
            .amount(new BigDecimal("12000"))
            .deadline(LocalDate.of(2026, 5, 15))
            .description("Supporting the next generation of science and technology leaders through substantial financial assistance.")
            .eligibility("Undergraduate students enrolled in STEM programs with strong academic standing.")
            .gpaRequirement(3.7)
            .seats(50)
            .renewable(false)
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Creative Arts Grant")
            .provider("National Arts Council")
            .category("Arts")
            .amount(new BigDecimal("5000"))
            .deadline(LocalDate.of(2026, 7, 20))
            .description("Empowering creative minds to pursue their artistic passions through dedicated financial support.")
            .eligibility("Students enrolled in fine arts, music, theatre, or creative writing programs.")
            .gpaRequirement(3.0)
            .seats(30)
            .renewable(false)
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Future Business Leaders Fund")
            .provider("Chamber of Commerce")
            .category("Business")
            .amount(new BigDecimal("10000"))
            .deadline(LocalDate.of(2026, 8, 1))
            .description("Investing in future business leaders who demonstrate entrepreneurial spirit and academic excellence.")
            .eligibility("Business and economics students with leadership experience.")
            .gpaRequirement(3.3)
            .seats(25)
            .renewable(true)
            .renewalCriteria("Remain enrolled in business-related program with minimum 3.3 GPA")
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Healthcare Heroes Scholarship")
            .provider("National Health Foundation")
            .category("Healthcare")
            .amount(new BigDecimal("15000"))
            .deadline(LocalDate.of(2026, 5, 31))
            .description("For aspiring healthcare professionals committed to making a difference in patient care and community health.")
            .eligibility("Students in nursing, medicine, pharmacy, or allied health programs.")
            .gpaRequirement(3.5)
            .seats(40)
            .renewable(true)
            .renewalCriteria("Maintain 3.5 GPA and complete 20 volunteer hours per semester")
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Engineering Innovation Award")
            .provider("Society of Engineers")
            .category("Engineering")
            .amount(new BigDecimal("20000"))
            .deadline(LocalDate.of(2026, 9, 30))
            .description("Recognizing exceptional engineering students who push boundaries and develop solutions to real-world problems.")
            .eligibility("Junior or senior engineering students with a demonstrated project portfolio.")
            .gpaRequirement(3.6)
            .seats(15)
            .renewable(false)
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Legal Eagle Scholarship")
            .provider("American Bar Foundation")
            .category("Law")
            .amount(new BigDecimal("18000"))
            .deadline(LocalDate.of(2026, 10, 15))
            .description("Empowering the next generation of legal professionals who will champion justice and equality.")
            .eligibility("Pre-law or law school students with outstanding academic achievement.")
            .gpaRequirement(3.7)
            .seats(20)
            .renewable(false)
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Diversity in Education Award")
            .provider("Education Equity Alliance")
            .category("Education")
            .amount(new BigDecimal("7500"))
            .deadline(LocalDate.of(2026, 11, 1))
            .description("Celebrating and supporting diverse voices in the field of education and teaching.")
            .eligibility("Education majors from underrepresented backgrounds.")
            .gpaRequirement(3.0)
            .seats(60)
            .renewable(true)
            .renewalCriteria("Maintain 3.0 GPA and remain in education program")
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        scholarshipRepository.save(Scholarship.builder()
            .title("Sports Leadership Grant")
            .provider("National Athletics Foundation")
            .category("Sports")
            .amount(new BigDecimal("6000"))
            .deadline(LocalDate.of(2026, 12, 15))
            .description("Recognizing outstanding student-athletes who excel both academically and in their sport.")
            .eligibility("Student athletes with varsity experience and a minimum 2.8 GPA.")
            .gpaRequirement(2.8)
            .seats(45)
            .renewable(false)
            .status(Scholarship.ScholarshipStatus.ACTIVE)
            .build());

        log.info("Seeded 9 scholarships into database");
    }

    // ── Financial Aid ──────────────────────────────────────────────────────────

    private void seedFinancialAids() {
        if (financialAidRepository.count() > 0) {
            log.info("Financial aids already seeded ({} records)", financialAidRepository.count());
            return;
        }

        financialAidRepository.save(FinancialAid.builder()
            .title("Emergency Student Fund")
            .category("Emergency")
            .amount(new BigDecimal("2500"))
            .description("Immediate financial assistance for students facing unexpected hardship such as medical emergencies or housing insecurity.")
            .maxApplicants(200)
            .deadline(LocalDate.of(2026, 12, 31))
            .status(FinancialAid.AidStatus.ACTIVE)
            .build());

        financialAidRepository.save(FinancialAid.builder()
            .title("Textbook & Supplies Grant")
            .category("Academic")
            .amount(new BigDecimal("800"))
            .description("Help covering the cost of required textbooks and course materials for the upcoming semester.")
            .maxApplicants(500)
            .deadline(LocalDate.of(2026, 9, 1))
            .status(FinancialAid.AidStatus.ACTIVE)
            .build());

        financialAidRepository.save(FinancialAid.builder()
            .title("Housing Assistance Program")
            .category("Housing")
            .amount(new BigDecimal("5000"))
            .description("Subsidized housing support for students who demonstrate significant financial need and are at risk of housing insecurity.")
            .maxApplicants(100)
            .deadline(LocalDate.of(2026, 7, 31))
            .status(FinancialAid.AidStatus.ACTIVE)
            .build());

        log.info("Seeded 3 financial aid records into database");
    }
}
