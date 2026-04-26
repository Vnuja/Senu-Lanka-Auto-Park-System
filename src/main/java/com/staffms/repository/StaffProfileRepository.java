package com.staffms.repository;

import com.staffms.model.entity.StaffProfile;
import com.staffms.model.entity.Department;
import com.staffms.util.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
    Optional<StaffProfile> findByUser_UserId(Long userId);
    List<StaffProfile> findByDepartment(Department department);
    List<StaffProfile> findByEmploymentType(EmploymentType type);
    List<StaffProfile> findByFullNameContainingIgnoreCase(String name);
}
