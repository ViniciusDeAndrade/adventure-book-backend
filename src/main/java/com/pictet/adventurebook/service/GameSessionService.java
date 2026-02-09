package com.pictet.adventurebook.service;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Consequence;
import com.pictet.adventurebook.domain.ConsequenceType;
import com.pictet.adventurebook.domain.GameSession;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionType;
import com.pictet.adventurebook.exception.GameOverException;
import com.pictet.adventurebook.exception.InvalidChoiceException;
import com.pictet.adventurebook.exception.ResourceNotFoundException;
import com.pictet.adventurebook.repository.GameSessionRepository;
import com.pictet.adventurebook.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameSessionService {

    private final GameSessionRepository sessionRepository;
    private final SectionRepository sectionRepository;
    private final BookService bookService;
    private final MessageSource messageSource;

    @Transactional
    public GameSession startGame(Long bookId) {
        Book book = bookService.getById(bookId);
        Section start = book.getSections().stream()
                .filter(s -> s.getType() == SectionType.BEGIN)
                .findFirst()
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("error.book.noBegin", null, Locale.getDefault());
                    log.warn(messageSource.getMessage("log.exception.invalidBook", new Object[]{msg}, Locale.getDefault()));
                    return new ResourceNotFoundException(msg);
                });
        GameSession session = GameSession.builder()
                .book(book)
                .currentSection(start)
                .health(GameSession.INITIAL_HEALTH)
                .build();
        session = sessionRepository.save(session);
        log.info(messageSource.getMessage("log.session.created", new Object[]{session.getId(), bookId}, Locale.getDefault()));
        return session;
    }

    @Transactional(readOnly = true)
    public GameSession getById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("error.session.notFound", new Object[]{id}, Locale.getDefault());
                    log.warn(messageSource.getMessage("log.exception.notFound", new Object[]{msg}, Locale.getDefault()));
                    return new ResourceNotFoundException(msg);
                });
    }

    @Transactional(readOnly = true)
    public Section getCurrentSection(Long sessionId) {
        log.debug(messageSource.getMessage("log.session.current", new Object[]{sessionId}, Locale.getDefault()));
        GameSession session = getById(sessionId);
        return session.getCurrentSection();
    }

    @Transactional
    public Section makeChoice(Long sessionId, Integer gotoSectionId) {
        log.info(messageSource.getMessage("log.session.choice", new Object[]{sessionId, gotoSectionId}, Locale.getDefault()));
        GameSession session = getById(sessionId);
        if (session.getHealth() <= 0) {
            String msg = messageSource.getMessage("error.gameOver.dead", null, Locale.getDefault());
            log.warn(messageSource.getMessage("log.exception.gameOver", new Object[]{msg}, Locale.getDefault()));
            throw new GameOverException(msg);
        }
        Section currentSession = session.getCurrentSection();
        var option = currentSession.getOptions().stream()
                .filter(o -> gotoSectionId.equals(o.getGotoSectionId()))
                .findFirst()
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("error.choice.invalid", new Object[]{gotoSectionId}, Locale.getDefault());
                    log.warn(messageSource.getMessage("log.exception.invalidChoice", new Object[]{msg}, Locale.getDefault()));
                    return new InvalidChoiceException(msg);
                });

        int newHealth = session.getHealth();
        Consequence consequence = option.getConsequence();
        if (consequence != null) {
            if (consequence.getType() == ConsequenceType.LOSE_HEALTH) {
                newHealth = Math.max(0, newHealth - consequence.getValue());
            } else if (consequence.getType() == ConsequenceType.GAIN_HEALTH) {
                newHealth = Math.min(GameSession.INITIAL_HEALTH, newHealth + consequence.getValue());
            }
        }
        session.setHealth(newHealth);

        Section nextSession = sectionRepository.findByBookAndSectionId(session.getBook(), gotoSectionId)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("error.choice.sectionNotFound", new Object[]{gotoSectionId}, Locale.getDefault());
                    log.warn(messageSource.getMessage("log.exception.invalidChoice", new Object[]{msg}, Locale.getDefault()));
                    return new InvalidChoiceException(msg);
                });
        session.setCurrentSection(nextSession);
        sessionRepository.save(session);

        if (newHealth <= 0) {
            String msg = messageSource.getMessage("error.gameOver.died", null, Locale.getDefault());
            log.warn(messageSource.getMessage("log.exception.gameOver", new Object[]{msg}, Locale.getDefault()));
            throw new GameOverException(msg);
        }
        return nextSession;
    }
}
