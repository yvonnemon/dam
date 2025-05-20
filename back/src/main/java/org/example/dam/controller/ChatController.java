package org.example.dam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.dam.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class ChatController {

    private static Map<String, List<String>> replyKeys = new HashMap<>();

    @PostConstruct
    public void loadReplies() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/bot-replies.json");
        TypeReference<Map<String, List<String>>> typeRef = new TypeReference<>() {};
        replyKeys = mapper.readValue(is, typeRef);
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/replies")
    public ChatMessage handleMessage(ChatMessage message) throws Exception {
        Thread.sleep(500); // simulate thinking
        return generateBotReply(message);
    }

    private ChatMessage generateBotReply(ChatMessage incoming) {
        String content = incoming.getContent().toLowerCase();
        String intent = detectIntent(content);

        List<String> replies = replyKeys.getOrDefault(intent, replyKeys.get("unknown"));
        String key = replies.get(new Random().nextInt(replies.size()));

        ChatMessage response = new ChatMessage();
        response.setSender("HelpBot");
        response.setContent(key); // ← return reply key
        return response;
    }


    private String detectIntent(String content) {
        for (Map.Entry<String, List<String>> entry : intentKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (content.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return "unknown";
    }

    private static final Map<String, List<String>> intentKeywords = Map.of(
            "greeting", List.of("hello", "hi", "hey", "hola", "ola"),
            "price", List.of("price", "cost", "how much", "fee", "precio", "costo", "cuanto"),
            "cancel", List.of("cancel", "cancelation", "cancellation", "cancelar"),
            "refund", List.of("refund", "money back", "reimbursement", "reembolso", "devolucion"),
            "unknown", List.of() // fallback
    );
}
