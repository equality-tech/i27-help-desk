package com.i27.helpdesk.ticket.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_activities")
public class TicketActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private String action; // CREATED, ASSIGNED, UNASSIGNED, STATUS_CHANGED

    @Column(name = "performed_by")
    private Long performedBy; // user_id (admin / agent / student)

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // =========================
    // JPA lifecycle
    // =========================
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // =========================
    // GETTERS
    // =========================
    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getAction() {
        return action;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // =========================
    // SETTERS  🔥 THIS FIXES EVERYTHING
    // =========================
    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
