package TestComponents;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackNotifier {
    private static final String WEBHOOK_URL = getWebhookUrl();// paste here
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
            conn.setRequestProperty("Content-Type", "application/json");

            String payload = "{\"text\":\"" + message + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("utf-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("⚠️ Slack notification failed. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}