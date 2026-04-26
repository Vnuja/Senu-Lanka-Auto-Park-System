package com.staffms.service;

import com.staffms.model.entity.StaffProfile;
import com.staffms.model.entity.Department;
import com.staffms.repository.StaffProfileRepository;
import com.staffms.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffProfileRepository staffProfileRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<StaffProfile> getAllStaff() {
        return staffProfileRepository.findAll();
    }

    public Optional<StaffProfile> getStaffById(Long id) {
        return staffProfileRepository.findById(id);
    }

    public Optional<StaffProfile> getStaffByUserId(Long userId) {
        return staffProfileRepository.findByUser_UserId(userId);
    }

    @Transactional
    public StaffProfile saveStaffProfile(StaffProfile profile) {
        return staffProfileRepository.save(profile);
    }

    @Transactional
    public void deleteStaffProfile(Long id) {
        staffProfileRepository.deleteById(id);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }
}
