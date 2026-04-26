package com.staffms.service;

import com.staffms.model.entity.User;
import com.staffms.model.entity.SystemLog;
import com.staffms.repository.UserRepository;
import com.staffms.repository.SystemLogRepository;
import com.staffms.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemLogRepository logRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User savedUser = userRepository.save(user);
        
        logAction(null, "CREATE_USER", "User", savedUser.getUserId());
        return savedUser;
    }

    @Transactional
    public User updateUser(User user) {
        logAction(null, "UPDATE_USER", "User", user.getUserId());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        logAction(null, "DELETE_USER", "User", id);
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logAction(userId, "CHANGE_PASSWORD", "User", userId);
    }

    private void logAction(Long userId, String action, String entity, Long entityId) {
        SystemLog log = SystemLog.builder()
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .user(userId != null ? userRepository.findById(userId).orElse(null) : null)
                .build();
        logRepository.save(log);
    }
}
