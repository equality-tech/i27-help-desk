package com.i27.helpdesk.ticket.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${notification.service.url}")
    private String notificationUrl;

    @Async
    public void sendTicketCreatedNotification(Long ticketId, Long studentId) {
        send(Map.of(
                "event_type", "TICKET_CREATED",
                "ticket_id", ticketId,
                "actor_id", studentId,
                "actor_role", "STUDENT",
                "recipient_id", studentId
        ));
    }

    @Async
    public void sendTicketAssignedToAgent(Long ticketId, Long agentId) {
        send(Map.of(
                "event_type", "TICKET_ASSIGNED_AGENT",
                "ticket_id", ticketId,
                "actor_id", agentId,
                "actor_role", "AGENT",
                "recipient_id", agentId
        ));
    }

    @Async
    public void sendTicketAssignedToAdmin(Long ticketId, Long adminId) {
        send(Map.of(
                "event_type", "TICKET_ASSIGNED_ADMIN",
                "ticket_id", ticketId,
                "actor_id", adminId,
                "actor_role", "ADMIN",
                "recipient_id", adminId
        ));
    }

    // 🆕 STATUS CHANGE → STUDENT
    @Async
    public void sendTicketStatusChanged(Long ticketId, Long studentId) {
        send(Map.of(
                "event_type", "TICKET_STATUS_CHANGED",
                "ticket_id", ticketId,
                "actor_id", studentId,
                "actor_role", "SYSTEM",
                "recipient_id", studentId
        ));
    }

    private void send(Map<String, Object> payload) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            restTemplate.postForEntity(
                    notificationUrl,
                    new HttpEntity<>(payload, headers),
                    Void.class
            );
        } catch (Exception ex) {
            System.out.println("⚠️ Notification failed: " + ex.getMessage());
        }
    }
}
