package com.staffms.repository;

import com.staffms.model.entity.Performance;
import com.staffms.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByUserOrderByCreatedAtDesc(User user);
}
