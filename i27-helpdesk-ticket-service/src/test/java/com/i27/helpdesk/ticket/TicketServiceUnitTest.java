package com.i27.helpdesk.ticket;

import com.i27.helpdesk.ticket.controller.HealthController;
import com.i27.helpdesk.ticket.model.Ticket;
import com.i27.helpdesk.ticket.model.TicketActivity;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TicketServiceUnitTest {

    @Test
    void healthReportsUp() {
        assertThat(new HealthController().health().getBody()).isEqualTo(Map.of("status", "UP"));
    }

    @Test
    void readinessReportsReady() {
        assertThat(new HealthController().ready().getBody()).isEqualTo(Map.of("status", "READY"));
    }

    @Test
    void ticketLifecycleSetsTimestamps() {
        Ticket ticket = new Ticket();
        ReflectionTestUtils.invokeMethod(ticket, "onCreate");

        assertThat(ReflectionTestUtils.getField(ticket, "createdAt")).isInstanceOf(LocalDateTime.class);
        assertThat(ReflectionTestUtils.getField(ticket, "updatedAt")).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void ticketLifecycleRefreshesUpdateTimestamp() {
        Ticket ticket = new Ticket();
        ReflectionTestUtils.invokeMethod(ticket, "onUpdate");

        assertThat(ReflectionTestUtils.getField(ticket, "updatedAt")).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void ticketActivityRetainsAssignedValues() {
        TicketActivity activity = new TicketActivity();
        activity.setTicketId(42L);
        activity.setAction("CREATED");
        activity.setPerformedBy(7L);
        activity.setMessage("Ticket created");

        assertThat(activity.getTicketId()).isEqualTo(42L);
        assertThat(activity.getAction()).isEqualTo("CREATED");
        assertThat(activity.getPerformedBy()).isEqualTo(7L);
        assertThat(activity.getMessage()).isEqualTo("Ticket created");
    }
}
