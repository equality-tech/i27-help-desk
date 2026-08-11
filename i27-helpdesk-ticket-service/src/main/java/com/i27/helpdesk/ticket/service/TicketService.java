package com.i27.helpdesk.ticket.service;

import com.i27.helpdesk.ticket.model.Ticket;
import com.i27.helpdesk.ticket.model.TicketActivity;
import com.i27.helpdesk.ticket.repository.TicketActivityRepository;
import com.i27.helpdesk.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketActivityRepository activityRepository;
    private final NotificationClient notificationClient;

    public TicketService(
            TicketRepository ticketRepository,
            TicketActivityRepository activityRepository,
            NotificationClient notificationClient
    ) {
        this.ticketRepository = ticketRepository;
        this.activityRepository = activityRepository;
        this.notificationClient = notificationClient;
    }

    // ==============================
    // 🎫 CREATE TICKET (STUDENT)
    // ==============================
    public Ticket createTicket(Ticket ticket) {

        ticket.setStatus("OPEN");

        if (ticket.getPriority() == null) {
            ticket.setPriority("MEDIUM");
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        notificationClient.sendTicketCreatedNotification(
                savedTicket.getId(),
                savedTicket.getCreatedBy()
        );

        recordActivity(
                savedTicket.getId(),
                "CREATED",
                savedTicket.getCreatedBy(),
                "Ticket created"
        );

        return savedTicket;
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketRepository.findByCreatedBy(userId);
    }

    public List<Ticket> getTicketsByAgent(Long agentId) {
        return ticketRepository.findByAssignedTo(agentId);
    }

    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // ==============================
    // 🔐 ADMIN → ASSIGN / UNASSIGN
    // ==============================
    public Ticket assignAgent(Long ticketId, Long agentId, Long adminId) {

        Ticket ticket = getTicketById(ticketId);

        if (agentId == null) {
            ticket.setAssignedTo(null);
            ticket.setStatus("OPEN");

            recordActivity(ticketId, "UNASSIGNED", adminId, "Ticket unassigned");

        } else {
            ticket.setAssignedTo(agentId);
            ticket.setStatus("IN_PROGRESS");

            notificationClient.sendTicketAssignedToAgent(ticketId, agentId);
            notificationClient.sendTicketAssignedToAdmin(ticketId, adminId);

            recordActivity(ticketId, "ASSIGNED", adminId, "Ticket assigned to agent");
        }

        return ticketRepository.save(ticket);
    }

    // ==============================
    // 🔄 UPDATE STATUS / PRIORITY
    // ==============================
    public Ticket updateTicket(
            Long ticketId,
            String status,
            String priority,
            Long performedBy
    ) {

        Ticket ticket = getTicketById(ticketId);

        boolean statusChanged = false;
        boolean priorityChanged = false;

        if (status != null && !status.equals(ticket.getStatus())) {
            ticket.setStatus(status);
            statusChanged = true;
        }

        if (priority != null && !priority.equals(ticket.getPriority())) {
            ticket.setPriority(priority);
            priorityChanged = true;
        }

        Ticket updatedTicket = ticketRepository.save(ticket);

        if (statusChanged) {
            recordActivity(
                    ticketId,
                    "STATUS_CHANGED",
                    performedBy,
                    "Status changed to " + updatedTicket.getStatus()
            );

            // 🔔 NOTIFY STUDENT
            notificationClient.sendTicketStatusChanged(
                    ticketId,
                    ticket.getCreatedBy()
            );
        }

        if (priorityChanged) {
            recordActivity(
                    ticketId,
                    "PRIORITY_CHANGED",
                    performedBy,
                    "Priority changed to " + updatedTicket.getPriority()
            );
        }

        return updatedTicket;
    }

    public List<TicketActivity> getTicketActivities(Long ticketId) {
        return activityRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }

    private void recordActivity(
            Long ticketId,
            String action,
            Long performedBy,
            String message
    ) {
        TicketActivity activity = new TicketActivity();
        activity.setTicketId(ticketId);
        activity.setAction(action);
        activity.setPerformedBy(performedBy);
        activity.setMessage(message);

        activityRepository.save(activity);
    }
}
