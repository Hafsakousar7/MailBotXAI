package com.email.writer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailGeneretorController {
	private final EmailGeneratorService emailGeneratorService;

	public EmailGeneretorController(EmailGeneratorService emailGeneratorService) {
		this.emailGeneratorService = emailGeneratorService;

	}

	@PostMapping("/generate")
	public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
		String response = emailGeneratorService.generetorEmailReply(emailRequest);

		return ResponseEntity.ok(response);

	}

}
