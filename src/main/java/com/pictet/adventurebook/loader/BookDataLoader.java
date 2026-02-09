package com.pictet.adventurebook.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.loader.dto.BookImportDto;
import com.pictet.adventurebook.loader.mapper.BookImportMapper;
import com.pictet.adventurebook.repository.BookRepository;
import com.pictet.adventurebook.service.BookValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Locale;

@Component
@ConditionalOnProperty(name = "book.loader.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class BookDataLoader {

    private final BookRepository bookRepository;
    private final BookValidationService bookValidationService;
    private final BookImportMapper bookImportMapper;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void loadBooks() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:books/*.json");
        for (Resource resource : resources) {
            try {
                BookImportDto dto = objectMapper.readValue(resource.getInputStream(), BookImportDto.class);
                if (dto.getSections() == null || dto.getSections().isEmpty()) {
                    log.warn(messageSource.getMessage("log.loader.skippingNoSections",
                            new Object[]{resource.getFilename()}, Locale.getDefault()));
                    continue;
                }
                Book book = bookImportMapper.toBook(dto);
                bookValidationService.validate(book);
                bookRepository.save(book);
                log.info(messageSource.getMessage("log.loader.loaded", new Object[]{book.getTitle()}, Locale.getDefault()));
            } catch (Exception e) {
                log.warn(messageSource.getMessage("log.loader.failed",
                        new Object[]{resource.getFilename(), e.getMessage()}, Locale.getDefault()));
            }
        }
    }
}
