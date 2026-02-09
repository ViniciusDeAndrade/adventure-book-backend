package com.pictet.adventurebook.api;

import com.pictet.adventurebook.api.dto.ChoiceRequestDto;
import com.pictet.adventurebook.api.dto.GameSessionDto;
import com.pictet.adventurebook.api.dto.NewSessionDto;
import com.pictet.adventurebook.api.dto.SectionReadDto;
import com.pictet.adventurebook.api.mapper.GameSessionMapper;
import com.pictet.adventurebook.api.mapper.SectionMapper;
import com.pictet.adventurebook.domain.GameSession;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.service.GameSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions")
@Slf4j
public class SessionController {

    private final GameSessionService gameSessionService;
    private final GameSessionMapper gameSessionMapper;
    private final SectionMapper sectionMapper;

    @PostMapping
    @Operation(summary = "Start a new game session")
    public ResponseEntity<GameSessionDto> create(@Valid @RequestBody NewSessionDto body) {
        GameSession session = gameSessionService.startGame(body.getBookId());
        GameSessionDto dto = gameSessionMapper.toDto(session);
        return ResponseEntity.created(URI.create("/api/sessions/" + session.getId())).body(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session state")
    public ResponseEntity<GameSessionDto> getById(@PathVariable Long id) {
        GameSession session = gameSessionService.getById(id);
        return ResponseEntity.ok(gameSessionMapper.toDto(session));
    }

    @GetMapping("/{id}/current")
    @Operation(summary = "Get current section")
    public ResponseEntity<SectionReadDto> getCurrent(@PathVariable Long id) {
        Section section = gameSessionService.getCurrentSection(id);
        return ResponseEntity.ok(sectionMapper.toReadDto(section));
    }

    @PostMapping("/{id}/choices")
    @Operation(summary = "Make a choice and advance")
    public ResponseEntity<SectionReadDto> makeChoice(
            @PathVariable Long id,
            @Valid @RequestBody ChoiceRequestDto body) {
        Section section = gameSessionService.makeChoice(id, body.getGotoSectionId());
        return ResponseEntity.ok(sectionMapper.toReadDto(section));
    }
}
