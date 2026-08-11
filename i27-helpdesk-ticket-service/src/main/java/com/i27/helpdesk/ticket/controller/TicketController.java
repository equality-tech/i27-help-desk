package com.i27.helpdesk.ticket.controller;

import com.i27.helpdesk.ticket.model.Ticket;
import com.i27.helpdesk.ticket.model.TicketActivity;
import com.i27.helpdesk.ticket.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ==============================
    // 👨‍🎓 STUDENT → MY TICKETS
    // ==============================
@GetMapping("/me")
public ResponseEntity<?> getMyTickets(HttpServletRequest request) {

    String userIdHeader = request.getHeader("X-User-Id");
    if (userIdHeader == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(
            ticketService.getTicketsByUser(Long.valueOf(userIdHeader))
    );
}


    // ==============================
    // 👨‍💼 AGENT → MY ASSIGNED TICKETS
    // ==============================
@GetMapping("/assigned-to-me")
public ResponseEntity<?> getMyAssignedTickets(HttpServletRequest request) {

    String userIdHeader = request.getHeader("X-User-Id");
    String roleHeader = request.getHeader("X-User-Role");

    if (userIdHeader == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (!"AGENT".equals(roleHeader)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(
            ticketService.getTicketsByAgent(Long.valueOf(userIdHeader))
    );
}



    // ==============================
    // 👨‍🎓 CREATE TICKET
    // ==============================
@PostMapping
public ResponseEntity<Ticket> createTicket(
        @RequestBody Ticket ticket,
        HttpServletRequest request
) {
    String userIdHeader = request.getHeader("X-User-Id");
    if (userIdHeader == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    ticket.setCreatedBy(Long.valueOf(userIdHeader));

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ticketService.createTicket(ticket));
}

    // ==============================
    // 🔐 GET TICKETS (ADMIN / AGENT)
    // ==============================
    @GetMapping
    public ResponseEntity<?> getTickets(
            @RequestParam(required = false) Long createdBy,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) String status
    ) {

        if (createdBy == null && assignedTo == null && status == null) {
            return ResponseEntity.ok(ticketService.getAllTickets());
        }

        if (createdBy != null) {
            return ResponseEntity.ok(ticketService.getTicketsByUser(createdBy));
        }

        if (assignedTo != null) {
            return ResponseEntity.ok(ticketService.getTicketsByAgent(assignedTo));
        }

        if (status != null) {
            return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
        }

        return ResponseEntity.badRequest().build();
    }

// ==============================
// 🔐 ASSIGN / UNASSIGN (ADMIN)
// ==============================
@PutMapping("/{id}/assign")
public ResponseEntity<?> assignTicket(
        @PathVariable Long id,
        @RequestBody Map<String, Long> request,
        HttpServletRequest httpRequest
) {
    String adminIdHeader = httpRequest.getHeader("X-User-Id");

    if (adminIdHeader == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Long agentId = request.get("agentId");
    Long adminId = Long.valueOf(adminIdHeader);

    return ResponseEntity.ok(
            ticketService.assignAgent(id, agentId, adminId)
    );
}


    // ==============================
    // 🔄 UPDATE STATUS / PRIORITY (AGENT / ADMIN)
    // ==============================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest
    ) {

        String userIdHeader = httpRequest.getHeader("X-User-Id");
        if (userIdHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                ticketService.updateTicket(
                        id,
                        request.get("status"),
                        request.get("priority"),
                        Long.valueOf(userIdHeader)
                )
        );
    }

    // ==============================
    // 📄 GET TICKET BY ID
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    // ==============================
    // 🕒 ACTIVITY TIMELINE
    // ==============================
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<TicketActivity>> getTicketActivities(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ticketService.getTicketActivities(id)
        );
    }
}
