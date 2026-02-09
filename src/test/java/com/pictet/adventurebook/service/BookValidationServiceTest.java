package com.pictet.adventurebook.service;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Difficulty;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionOption;
import com.pictet.adventurebook.domain.SectionType;
import com.pictet.adventurebook.exception.InvalidBookException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookValidationServiceTest {

    @Mock
    private MessageSource messageSource;
    private BookValidationService service;

    @BeforeEach
    void setUp() {
        service = new BookValidationService(messageSource);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void validate_noBegin_throws() {
        Book book = validBook();
        book.getSections().get(0).setType(SectionType.NODE);
        assertThrows(InvalidBookException.class, () -> service.validate(book));
    }

    @Test
    void validate_twoBegins_throws() {
        Book book = validBook();
        book.getSections().get(1).setType(SectionType.BEGIN);
        assertThrows(InvalidBookException.class, () -> service.validate(book));
    }

    @Test
    void validate_noEnd_throws() {
        Book book = validBook();
        book.getSections().get(2).setType(SectionType.NODE);
        book.getSections().get(2).getOptions().add(SectionOption.builder()
                .description("x").gotoSectionId(1).section(book.getSections().get(2)).build());
        assertThrows(InvalidBookException.class, () -> service.validate(book));
    }

    @Test
    void validate_nodeWithoutOptions_throws() {
        Book book = validBook();
        book.getSections().get(1).getOptions().clear();
        assertThrows(InvalidBookException.class, () -> service.validate(book));
    }

    @Test
    void validate_invalidGotoId_throws() {
        Book book = validBook();
        book.getSections().get(0).getOptions().get(0).setGotoSectionId(999);
        assertThrows(InvalidBookException.class, () -> service.validate(book));
    }

    private static Book validBook() {
        Book book = Book.builder()
                .title("Test")
                .author("Author")
                .difficulty(Difficulty.EASY)
                .sections(new ArrayList<>())
                .build();
        Section begin = Section.builder().book(book).sectionId(1).text("Start").type(SectionType.BEGIN).options(new ArrayList<>()).build();
        begin.getOptions().add(SectionOption.builder().description("Go").gotoSectionId(2).section(begin).build());
        Section node = Section.builder().book(book).sectionId(2).text("Mid").type(SectionType.NODE).options(new ArrayList<>()).build();
        node.getOptions().add(SectionOption.builder().description("End").gotoSectionId(3).section(node).build());
        Section end = Section.builder().book(book).sectionId(3).text("End").type(SectionType.END).options(new ArrayList<>()).build();
        book.getSections().addAll(List.of(begin, node, end));
        return book;
    }
}
