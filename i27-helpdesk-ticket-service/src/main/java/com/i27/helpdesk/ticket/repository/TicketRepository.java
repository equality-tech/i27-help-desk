package com.i27.helpdesk.ticket.repository;

import com.i27.helpdesk.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Get all tickets created by a user
    List<Ticket> findByCreatedBy(Long createdBy);

    // Get all tickets assigned to an agent
    List<Ticket> findByAssignedTo(Long assignedTo);

    // Get tickets by status
    List<Ticket> findByStatus(String status);
}
