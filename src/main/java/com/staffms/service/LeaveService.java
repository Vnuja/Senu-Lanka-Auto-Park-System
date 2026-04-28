package com.staffms.service;

import com.staffms.model.entity.LeaveRequest;
import com.staffms.model.entity.User;
import com.staffms.repository.LeaveRequestRepository;
import com.staffms.util.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private SystemLogService logService;

    public List<LeaveRequest> getLeavesByUser(User user) {
        return leaveRequestRepository.findByUserOrderByStartDateDesc(user);
    }

    public List<LeaveRequest> getPendingLeaves() {
        return leaveRequestRepository.findByStatus(RequestStatus.PENDING);
    }

    @Transactional
    public LeaveRequest submitLeave(LeaveRequest request) {
        request.setStatus(RequestStatus.PENDING);
        LeaveRequest saved = leaveRequestRepository.save(request);
        logService.log(request.getUser(), "SUBMIT_LEAVE", "LeaveRequest", saved.getLeaveId());
        return saved;
    }

    public LeaveRequest getLeaveById(Long id) {
        return leaveRequestRepository.findById(id).orElseThrow();
    }

    @Transactional
    public LeaveRequest reviewLeave(Long leaveId, RequestStatus status, User reviewer) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId).orElseThrow();
        request.setStatus(status);
        request.setReviewedBy(reviewer);
        request.setReviewedAt(LocalDateTime.now());
        LeaveRequest saved = leaveRequestRepository.save(request);
        logService.log(reviewer, status.name() + "_LEAVE", "LeaveRequest", saved.getLeaveId());
        return saved;
    }
}
