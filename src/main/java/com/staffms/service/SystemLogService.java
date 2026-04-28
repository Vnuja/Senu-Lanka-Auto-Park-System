package com.staffms.service;

import com.staffms.model.entity.SystemLog;
import com.staffms.model.entity.User;
import com.staffms.repository.SystemLogRepository;
import com.staffms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemLogService {

    @Autowired
    private SystemLogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    public void log(User user, String action, String entity, Long entityId) {
        SystemLog log = SystemLog.builder()
                .user(user)
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .build();
        logRepository.save(log);
    }

    public List<SystemLog> getAllLogs() {
        return logRepository.findByOrderByTimestampDesc();
    }
    
    public List<SystemLog> getRecentLogs(int limit) {
        // Simple implementation for now
        return logRepository.findByOrderByTimestampDesc().stream().limit(limit).toList();
    }
}
