package com.example.serverside.slack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for sending notifications to Slack.
 * This class utilizes a Slack webhook URL to send messages.
 */
@Service
public class SlackNotificationService {

    /**
     * The URL of the Slack webhook used for sending messages.
     */

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    private static final Logger logger = LoggerFactory.getLogger(SlackNotificationService.class);

    /**
     * RestTemplate used for making HTTP requests to the Slack webhook.
     */
    private final RestTemplate restTemplate;

    /**
     * Constructor for SlackNotificationService.
     * Initializes a new RestTemplate for HTTP requests.
     */
    public SlackNotificationService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Sends a message to Slack using the configured webhook URL.
     *
     * @param message The message to be sent to Slack.
     */
    public void send(String message) {
        SlackMessage slackMessage = new SlackMessage(message);
        try {
            restTemplate.postForObject(slackWebhookUrl, slackMessage, String.class);
        } catch (Exception e) {
            logger.error("Failed to send Slack message: {}", e.getMessage(), e);
            throw new RuntimeException("Error sending notification to Slack", e);
        }
    }

    // Constructor for testing with a mock RestTemplate
    SlackNotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    static class SlackMessage {
        private String text;

        /**
         * Constructor for SlackMessage.
         *
         * @param text The text of the Slack message.
         */
        public SlackMessage(String text) {
            this.text = text;
        }

        /**
         * Gets the text of the Slack message.
         *
         * @return The text of the message.
         */
        public String getText() {
            return text;
        }

        /**
         * Sets the text of the Slack message.
         *
         * @param text The text to set for this message.
         */
        public void setText(String text) {
            this.text = text;
        }
    }
}
