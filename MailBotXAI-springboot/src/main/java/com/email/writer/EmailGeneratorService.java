package com.email.writer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailGeneratorService {

	private final WebClient webClient;

	public EmailGeneratorService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	@Value("${gemini.api.url}")
	private String geminiApiUrl;

	@Value("${gemini.api.key}")
	private String geminiApiKey;

	public String generetorEmailReply(EmailRequest emailRequest) {
		// Build the prompt
		String prompt = buildPrompt(emailRequest);

		// Correct request body format
		Map<String, Object> requestBody = Map.of("contents",
				List.of(Map.of("role", "user", "parts", List.of(Map.of("text", prompt)))));

		try {
			String response = webClient.post().uri(geminiApiUrl + "?key=" + geminiApiKey)
					.header("Content-Type", "application/json").bodyValue(requestBody).retrieve()
					.bodyToMono(String.class).block();

			return extractResponseContent(response);

		} catch (Exception e) {
			return "Error calling Gemini API: " + e.getMessage();
		}
	}

	private String extractResponseContent(String response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(response);
			return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
		} catch (Exception e) {
			return "Error processing response: " + e.getMessage();
		}
	}

	private String buildPrompt(EmailRequest emailRequest) {
		StringBuilder prompt = new StringBuilder();
		// prompt.append("Generate a professional email reply for the following email
		// content. ");
		prompt.append("You are an expert email assistant. \r\n"
				+ "Your task is to generate a professional email reply based on the original email content provided. \r\n"
				+ "- Maintain the tone specified by the user (e.g., friendly, formal, polite, enthusiastic).  \r\n"
				+ "- Keep the response clear, concise, and grammatically correct.  \r\n"
				+ "- Focus only on replying to the content of the email.  \r\n" + "");
		prompt.append("Please don't add a subject line. ");
		if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
			prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
		}
		prompt.append("\nOriginal email:\n").append(emailRequest.getEmailContent());
		return prompt.toString();
	}
}
