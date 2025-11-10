package TestComponents;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackNotifier {

    // Static webhook variable (initialized via helper method)
    private static final String WEBHOOK_URL = getWebhookUrl();

    // Helper method to safely fetch the webhook (env or system prop)
    private static String getWebhookUrl() {
        String envUrl = System.getenv("SLACK_WEBHOOK_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            return envUrl.trim();
        }

        String propUrl = System.getProperty("SLACK_WEBHOOK_URL");
        if (propUrl != null && !propUrl.isEmpty()) {
            return propUrl.trim();
        }

        return ""; // default: disabled
    }

    // Public method used by SlackTestListener
    public static void sendMessage(String message) {
        if (WEBHOOK_URL.isEmpty()) {
            System.out.println("SlackNotifier disabled (no webhook configured).");
            return;
        }
        try {
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String payload = "{\"text\":\"" + escapeJson(message) + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                System.err.println("⚠️ Slack notification failed. Response code: " + responseCode);
            }
        } catch (Exception e) {
            // keep logs concise
            System.err.println("⚠️ SlackNotifier error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    // Utility for safe JSON escaping
    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
