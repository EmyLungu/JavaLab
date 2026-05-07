package ro.uaic.homework;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LLMBot
 */
public class LLMBot extends Bot {
    protected String getAnswer(String question) {
        String apiKey = "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";
        String jsonBody = String.format("""
            {
                "model": "llama-3.3-70b-versatile",
                "messages": [{"role": "user", "content": "%s"}]
            }
            """, question + " Respond with EXACTLY ONE NUMBER!");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            Pattern pattern = Pattern.compile("\"content\"\\s*:\\s*\"(.*?)\"");
            Matcher matcher = pattern.matcher(response.body());

            if (matcher.find()) {
                String content = matcher.group(1);
                content = content.replace("\\n", "\n").replace("\\\"", "\"");

                return content;
            }
        } catch (Exception e) {
            System.out.println("Could not fetch response from the external LLM: " + e);
        }

        Integer value = 3;
        return value.toString();
    }

    public static void main(String[] args) {
        try {
            Bot bot = new LLMBot();
            bot.run();
        } catch (Exception e){
            System.err.println("Error inside the Bot: " + e);
        }
    }
}
