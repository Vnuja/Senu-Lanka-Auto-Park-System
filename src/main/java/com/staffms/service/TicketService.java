package com.staffms.service;

import com.staffms.model.entity.SupportTicket;
import com.staffms.model.entity.User;
import com.staffms.repository.SupportTicketRepository;
import com.staffms.util.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private SupportTicketRepository ticketRepository;

    public List<SupportTicket> getTicketsByUser(User user) {
        return ticketRepository.findByRaisedByOrderByCreatedAtDesc(user);
    }

    public List<SupportTicket> getAssignedTickets(User user) {
        return ticketRepository.findByAssignedToOrderByCreatedAtDesc(user);
    }

    public List<SupportTicket> getAllOpenTickets() {
        return ticketRepository.findByStatus(TicketStatus.OPEN);
    }

    public List<SupportTicket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional
    public SupportTicket createTicket(SupportTicket ticket) {
        ticket.setStatus(TicketStatus.OPEN);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public SupportTicket updateTicketStatus(Long ticketId, TicketStatus status) {
        SupportTicket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public SupportTicket assignTicket(Long ticketId, User supportOfficer) {
        SupportTicket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setAssignedTo(supportOfficer);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        return ticketRepository.save(ticket);
    }
}
