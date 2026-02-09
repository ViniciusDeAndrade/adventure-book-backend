package com.pictet.adventurebook.service;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionType;
import com.pictet.adventurebook.exception.InvalidBookException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookValidationService {

    private final MessageSource messageSource;

    public void validate(Book book) {

        validateBookBeginning(book);

        validateBookEnding(book);

        validateSections(book);

        log.debug("Book validation passed: {}", book.getTitle());
    }

    private void validateBookEnding(Book book) {
        long endCount = book.getSections().stream()
                .filter(s -> s.getType() == SectionType.END)
                .count();
        if (endCount == 0) {
            String msg = messageSource.getMessage("error.book.atLeastOneEnd", null, Locale.getDefault());
            log.warn("{}", msg);
            throw new InvalidBookException(msg);
        }
    }

    private void validateBookBeginning(Book book) {
        long beginCount = book.getSections().stream()
                .filter(s -> s.getType() == SectionType.BEGIN)
                .count();
        if (beginCount != 1) {
            String msg = messageSource.getMessage("error.book.oneBegin", null, Locale.getDefault());
            log.warn("{}", msg);
            throw new InvalidBookException(msg);
        }
    }

    private void validateSections(Book book) {
        Map<Integer, Section> byId = book.getSections().stream()
                .collect(Collectors.toMap(Section::getSectionId, s -> s));
        Set<Integer> validIds = byId.keySet();

        for (Section section : book.getSections()) {
            if (section.getType() != SectionType.END) {
                if (section.hasSectionOption()) {
                    String msg = messageSource.getMessage("error.book.nonEndSectionMustHaveOptions",
                            new Object[]{section.getSectionId()}, Locale.getDefault());
                    log.warn("{}", msg);
                    throw new InvalidBookException(msg);
                }
                for (var opt : section.getOptions()) {
                    if (opt.getGotoSectionId() != null && !validIds.contains(opt.getGotoSectionId())) {
                        String msg = messageSource.getMessage("error.book.invalidNextSectionId",
                                new Object[]{opt.getGotoSectionId()}, Locale.getDefault());
                        log.warn("{}", msg);
                        throw new InvalidBookException(msg);
                    }
                }
            }
        }
    }

}
