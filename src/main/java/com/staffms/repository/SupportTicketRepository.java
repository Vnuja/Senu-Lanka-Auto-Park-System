package com.staffms.repository;

import com.staffms.model.entity.SupportTicket;
import com.staffms.model.entity.User;
import com.staffms.util.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByRaisedByOrderByCreatedAtDesc(User user);
    List<SupportTicket> findByAssignedToOrderByCreatedAtDesc(User user);
    List<SupportTicket> findByStatus(TicketStatus status);
}
