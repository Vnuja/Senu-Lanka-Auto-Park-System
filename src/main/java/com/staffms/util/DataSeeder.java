package com.staffms.util;

import com.staffms.model.entity.Department;
import com.staffms.model.entity.StaffProfile;
import com.staffms.model.entity.User;
import com.staffms.repository.DepartmentRepository;
import com.staffms.repository.StaffProfileRepository;
import com.staffms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StaffProfileRepository staffProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // 1. Create Admin
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .email("admin@staffms.com")
                    .role(UserRole.SYSTEM_ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);

            // 2. Create Departments
            Department eng = departmentRepository.save(Department.builder().deptName("Engineering").build());
            Department hr = departmentRepository.save(Department.builder().deptName("Human Resources").build());
            Department ops = departmentRepository.save(Department.builder().deptName("Operations").build());

            // 3. Create Sample Staff (HR Manager)
            User hrUser = userRepository.save(User.builder()
                    .username("hr_manager")
                    .passwordHash(passwordEncoder.encode("hr123"))
                    .email("hr@staffms.com")
                    .role(UserRole.HR_MANAGER)
                    .build());
            
            staffProfileRepository.save(StaffProfile.builder()
                    .user(hrUser)
                    .fullName("Jane Doe")
                    .nic("199012345678")
                    .department(hr)
                    .jobTitle("HR Head")
                    .employmentType(EmploymentType.FULL_TIME)
                    .dateJoined(LocalDate.now().minusYears(2))
                    .salaryBase(new BigDecimal("75000"))
                    .build());

            // 4. Create Sample Staff (Engineering Manager)
            User engManagerUser = userRepository.save(User.builder()
                    .username("dept_manager")
                    .passwordHash(passwordEncoder.encode("mgr123"))
                    .email("manager@staffms.com")
                    .role(UserRole.DEPT_MANAGER)
                    .build());
            
            eng.setManager(engManagerUser);
            departmentRepository.save(eng);

            staffProfileRepository.save(StaffProfile.builder()
                    .user(engManagerUser)
                    .fullName("John Smith")
                    .nic("198512345678")
                    .department(eng)
                    .jobTitle("Engineering Manager")
                    .employmentType(EmploymentType.FULL_TIME)
                    .dateJoined(LocalDate.now().minusYears(3))
                    .salaryBase(new BigDecimal("95000"))
                    .build());

            // 5. Create Sample Staff (Staff)
            User staffUser = userRepository.save(User.builder()
                    .username("staff")
                    .passwordHash(passwordEncoder.encode("staff123"))
                    .email("staff@staffms.com")
                    .role(UserRole.STAFF)
                    .build());
            
            staffProfileRepository.save(StaffProfile.builder()
                    .user(staffUser)
                    .fullName("Bob Wilson")
                    .nic("199512345678")
                    .department(eng)
                    .jobTitle("Software Developer")
                    .employmentType(EmploymentType.FULL_TIME)
                    .dateJoined(LocalDate.now().minusMonths(6))
                    .salaryBase(new BigDecimal("60000"))
                    .build());
            
            System.out.println("Seed data successfully loaded.");
        }
        
        // Force activate all users to prevent "Account Disabled" issues from previous bad seeders
        userRepository.findAll().forEach(u -> {
            u.setActive(true);
            if (u.getUsername().equals("staff")) u.setPasswordHash(passwordEncoder.encode("staff123"));
            if (u.getUsername().equals("admin")) u.setPasswordHash(passwordEncoder.encode("admin123"));
            if (u.getUsername().equals("hr_manager")) u.setPasswordHash(passwordEncoder.encode("hr123"));
            if (u.getUsername().equals("dept_manager")) u.setPasswordHash(passwordEncoder.encode("mgr123"));
            userRepository.save(u);
        });
        System.out.println("Force activated all users and set default passwords for demo accounts.");
    }
}
