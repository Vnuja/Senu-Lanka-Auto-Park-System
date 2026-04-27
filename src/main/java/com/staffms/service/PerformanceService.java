package com.staffms.service;

import com.staffms.model.entity.Performance;
import com.staffms.model.entity.User;
import com.staffms.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;

    public List<Performance> getEvaluationsByUser(User user) {
        return performanceRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Performance submitEvaluation(Performance performance) {
        return performanceRepository.save(performance);
    }

    @Transactional
    public Performance updateEvaluation(Long id, String period, int rating, String comments) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));
        performance.setPeriod(period);
        performance.setRating(rating);
        performance.setComments(comments);
        return performanceRepository.save(performance);
    }

    @Transactional
    public void deleteEvaluation(Long id) {
        performanceRepository.deleteById(id);
    }
}
