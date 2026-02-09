package com.pictet.adventurebook.service;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Difficulty;
import com.pictet.adventurebook.exception.ResourceNotFoundException;
import com.pictet.adventurebook.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookValidationService bookValidationService;
    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    public List<Book> search(String title, String author, String category, Difficulty difficulty) {
        log.info(messageSource.getMessage("log.book.search",
                new Object[]{title, author, category, difficulty}, Locale.getDefault()));
        return bookRepository.search(title, author, category, difficulty);
    }

    @Transactional(readOnly = true)
    public Book getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("error.book.notFound", new Object[]{id}, Locale.getDefault());
                    log.warn(messageSource.getMessage("log.exception.notFound", new Object[]{msg}, Locale.getDefault()));
                    return new ResourceNotFoundException(msg);
                });
        log.debug(messageSource.getMessage("log.book.found", new Object[]{id}, Locale.getDefault()));
        return book;
    }

    @Transactional
    public void updateCategories(Long bookId, Set<String> categories) {
        Book book = getById(bookId);
        book.getCategories().clear();
        if (categories != null) {
            book.allCategories(categories);
        }
        bookRepository.save(book);
        log.info(messageSource.getMessage("log.book.categoriesUpdated", new Object[]{bookId}, Locale.getDefault()));
    }
}
