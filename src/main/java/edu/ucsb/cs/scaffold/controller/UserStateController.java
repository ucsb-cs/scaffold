package edu.ucsb.cs.scaffold.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucsb.cs.scaffold.model.UserState;
import edu.ucsb.cs.scaffold.repository.UserStateRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User State")
@RestController
@RequiredArgsConstructor
public class UserStateController {

    private final UserStateRepository userStateRepository;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Get saved user state by PIN")
    @GetMapping("/api/user-state/{pin}")
    public ResponseEntity<UserStateResponse> getUserState(
            @Parameter(description = "4-digit student PIN") @PathVariable String pin) {
        return userStateRepository.findByPin(pin)
                .map(state -> ResponseEntity.ok(new UserStateResponse(
                        parseStringList(state.getStarredIds()),
                        parseJsonNode(state.getDetailCards()),
                        parseStringList(state.getMasteredSubconcepts())
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Upsert saved user state by PIN")
    @PostMapping("/api/user-state")
    public ResponseEntity<Void> upsertUserState(@RequestBody UserStateRequest body) {
        if (body.pin() == null || body.pin().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        UserState state = userStateRepository.findByPin(body.pin()).orElseGet(UserState::new);
        state.setPin(body.pin());
        state.setStarredIds(writeJson(body.starred_ids() == null ? List.of() : body.starred_ids()));
        state.setDetailCards(writeJson(body.detail_cards() == null ? objectMapper.createArrayNode() : body.detail_cards()));
        state.setMasteredSubconcepts(writeJson(body.mastered_subconcepts() == null ? List.of() : body.mastered_subconcepts()));
        userStateRepository.save(state);

        return ResponseEntity.noContent().build();
    }

    private List<String> parseStringList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse stored string list JSON", e);
        }
    }

    private JsonNode parseJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse stored JSON payload", e);
        }
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize JSON payload", e);
        }
    }

    public record UserStateRequest(String pin, List<String> starred_ids, JsonNode detail_cards,
                                   List<String> mastered_subconcepts) {
    }

    public record UserStateResponse(List<String> starred_ids, JsonNode detail_cards,
                                    List<String> mastered_subconcepts) {
    }
}
