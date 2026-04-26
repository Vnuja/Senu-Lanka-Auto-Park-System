package com.staffms.repository;

import com.staffms.model.entity.LeaveRequest;
import com.staffms.model.entity.User;
import com.staffms.util.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUserOrderByStartDateDesc(User user);
    List<LeaveRequest> findByStatus(RequestStatus status);
}
