package com.pictet.adventurebook.service;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Consequence;
import com.pictet.adventurebook.domain.ConsequenceType;
import com.pictet.adventurebook.domain.GameSession;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionOption;
import com.pictet.adventurebook.domain.SectionType;
import com.pictet.adventurebook.exception.GameOverException;
import com.pictet.adventurebook.exception.InvalidChoiceException;
import com.pictet.adventurebook.exception.ResourceNotFoundException;
import com.pictet.adventurebook.repository.GameSessionRepository;
import com.pictet.adventurebook.repository.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameSessionServiceTest {

    @Mock
    private GameSessionRepository sessionRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private BookService bookService;
    @Mock
    private MessageSource messageSource;

    private GameSessionService gameSessionService;
    private Book book;
    private Section beginSection;
    private Section endSection;

    @BeforeEach
    void setUp() {
        gameSessionService = new GameSessionService(sessionRepository, sectionRepository, bookService, messageSource);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenAnswer(inv -> inv.getArgument(0));
        book = Book.builder().id(1L).title("Test").build();
        beginSection = Section.builder().book(book).sectionId(1).type(SectionType.BEGIN).options(new ArrayList<>()).build();
        beginSection.getOptions().add(SectionOption.builder().description("To end").gotoSectionId(2).section(beginSection).build());
        endSection = Section.builder().book(book).sectionId(2).type(SectionType.END).options(new ArrayList<>()).build();
        book.setSections(List.of(beginSection, endSection));
    }

    @Test
    void create_savesSessionWithInitialHealth() {
        when(bookService.getById(1L)).thenReturn(book);
        when(sessionRepository.save(any(GameSession.class))).thenAnswer(i -> {
            GameSession s = i.getArgument(0);
            s.setId(10L);
            return s;
        });
        GameSession session = gameSessionService.startGame(1L);
        assertEquals(GameSession.INITIAL_HEALTH, session.getHealth());
        assertEquals(beginSection, session.getCurrentSection());
        assertEquals(book, session.getBook());
        verify(sessionRepository).save(session);
    }

    @Test
    void getById_notFound_throws() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameSessionService.getById(1L));
    }

    @Test
    void makeChoice_appliesLoseHealthAndAdvances() {
        GameSession session = GameSession.builder().id(1L).book(book).currentSection(beginSection).health(10).build();
        SectionOption optionWithConsequence = SectionOption.builder()
                .description("Hurt").gotoSectionId(2).section(beginSection)
                .consequence(Consequence.builder().type(ConsequenceType.LOSE_HEALTH).value(3).text("Ouch").build())
                .build();
        beginSection.getOptions().clear();
        beginSection.getOptions().add(optionWithConsequence);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(GameSession.class))).thenAnswer(i -> i.getArgument(0));
        when(sectionRepository.findByBookAndSectionId(book, 2)).thenReturn(Optional.of(endSection));
        Section result = gameSessionService.makeChoice(1L, 2);
        assertEquals(endSection, result);
        assertEquals(7, session.getHealth());
    }

    @Test
    void makeChoice_invalidGotoId_throws() {
        GameSession session = GameSession.builder().id(1L).book(book).currentSection(beginSection).health(10).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertThrows(InvalidChoiceException.class, () -> gameSessionService.makeChoice(1L, 99));
    }

    @Test
    void makeChoice_deadPlayer_throwsGameOver() {
        GameSession session = GameSession.builder().id(1L).book(book).currentSection(beginSection).health(0).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        assertThrows(GameOverException.class, () -> gameSessionService.makeChoice(1L, 2));
    }

    @Test
    void makeChoice_loseHealthToZero_throwsGameOver() {
        GameSession session = GameSession.builder().id(1L).book(book).currentSection(beginSection).health(2).build();
        SectionOption option = SectionOption.builder()
                .description("Hurt").gotoSectionId(2).section(beginSection)
                .consequence(Consequence.builder().type(ConsequenceType.LOSE_HEALTH).value(5).text("").build())
                .build();
        beginSection.getOptions().clear();
        beginSection.getOptions().add(option);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sectionRepository.findByBookAndSectionId(book, 2)).thenReturn(Optional.of(endSection));
        assertThrows(GameOverException.class, () -> gameSessionService.makeChoice(1L, 2));
    }
}
