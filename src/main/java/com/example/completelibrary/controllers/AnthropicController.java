package com.example.completelibrary.controllers;


import com.example.completelibrary.dto.AiRequest;
import com.example.completelibrary.dto.AnthropicResponse;
import com.example.completelibrary.dto.RecommendRequest;
import com.example.completelibrary.dto.RecommendRespone;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/")
@RequiredArgsConstructor
public class AnthropicController {
    private final ChatClient chatClient;

    @PostMapping("/chat")
    public ResponseEntity<AnthropicResponse> responseEntity(@RequestBody AiRequest request){
        String response = chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
        AnthropicResponse anthropicResponse = new AnthropicResponse();
        anthropicResponse.setMessage(response);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(anthropicResponse);

    }
    @PostMapping("/recommend")
    public ResponseEntity<String> recommendation(@Valid @RequestBody RecommendRequest request){
        String prompt = String.format(
                "Recommend 3 books for someone who likes %s genre and is in a %s mood. " +
                        "For each book include title, author and why it matches.",
                request.getGenre(),
                request.getMood()
        );
        String response = chatClient.prompt()  //
                .user(prompt)
                .call()
                .content();
        return ResponseEntity.ok(response);
    }

}
