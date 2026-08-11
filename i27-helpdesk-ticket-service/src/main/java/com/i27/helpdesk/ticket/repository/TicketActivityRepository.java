package com.i27.helpdesk.ticket.repository;

import com.i27.helpdesk.ticket.model.TicketActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketActivityRepository
        extends JpaRepository<TicketActivity, Long> {

    List<TicketActivity> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
