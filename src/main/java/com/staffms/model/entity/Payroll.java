package com.staffms.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private BigDecimal basicSalary;

    private BigDecimal allowances = BigDecimal.ZERO;

    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal netSalary;

    @CreationTimestamp
    private LocalDateTime generatedAt;
}
