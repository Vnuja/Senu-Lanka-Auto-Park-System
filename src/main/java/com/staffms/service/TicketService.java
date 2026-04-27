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

    @Autowired
    private SystemLogService logService;

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
        SupportTicket saved = ticketRepository.save(ticket);
        logService.log(ticket.getRaisedBy(), "RAISE_TICKET", "SupportTicket", saved.getTicketId());
        return saved;
    }

    @Transactional
    public SupportTicket updateTicketStatus(Long ticketId, TicketStatus status) {
        SupportTicket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setStatus(status);
        SupportTicket saved = ticketRepository.save(ticket);
        logService.log(null, "UPDATE_TICKET_" + status.name(), "SupportTicket", ticketId);
        return saved;
    }

    @Transactional
    public SupportTicket assignTicket(Long ticketId, User supportOfficer) {
        SupportTicket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setAssignedTo(supportOfficer);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        SupportTicket saved = ticketRepository.save(ticket);
        logService.log(supportOfficer, "ASSIGN_TICKET", "SupportTicket", ticketId);
        return saved;
    }

    @Transactional
    public void deleteTicket(Long ticketId, User user) {
        ticketRepository.deleteById(ticketId);
        logService.log(user, "DELETE_TICKET", "SupportTicket", ticketId);
    }
}
