package com.example.serverside.slack.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class SlackMessageTest {

    @Test
    void testSlackMessageConstructorAndGetText() {
        String testText = "Test message";
        SlackNotificationService.SlackMessage slackMessage = new SlackNotificationService.SlackMessage(testText);

        assertEquals(testText, slackMessage.getText(), "The getText method should return the text set in the constructor.");
    }

    @Test
    void testSetText() {
        String initialText = "Initial message";
        SlackNotificationService.SlackMessage slackMessage = new SlackNotificationService.SlackMessage(initialText);

        String newText = "Updated message";
        slackMessage.setText(newText);

        assertEquals(newText, slackMessage.getText(), "The getText method should return the new text after setText is called.");
    }

    @Test
    void testSendWhenExceptionOccurs() {
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        doThrow(new RuntimeException("Simulated Exception")).when(mockRestTemplate)
                .postForObject(anyString(), any(), eq(String.class));

        SlackNotificationService service = new SlackNotificationService(mockRestTemplate);

        ReflectionTestUtils.setField(service, "slackWebhookUrl", "http://example.com/webhook");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.send("Test message");
        });

        // Verify the message of the thrown exception
        assertTrue(exception.getMessage().contains("Error sending notification to Slack"));
        assertEquals("Simulated Exception", exception.getCause().getMessage());
    }
}

