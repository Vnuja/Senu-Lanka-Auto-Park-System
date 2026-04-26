package com.staffms.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evalId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "evaluated_by", nullable = false)
    private User evaluatedBy;

    @Column(nullable = false, length = 50)
    private String period;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
