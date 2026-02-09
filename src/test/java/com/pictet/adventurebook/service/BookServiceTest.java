package com.pictet.adventurebook.service;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Difficulty;
import com.pictet.adventurebook.exception.ResourceNotFoundException;
import com.pictet.adventurebook.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookValidationService bookValidationService;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private BookService bookService;

    @Test
    void getById_notFound_throws() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Book not found: 1");
        assertThrows(ResourceNotFoundException.class, () -> bookService.getById(1L));
    }

    @Test
    void getById_found_returnsBook() {
        Book book = Book.builder().id(1L).title("T").author("A").difficulty(Difficulty.EASY).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("ok");
        assertEquals(book, bookService.getById(1L));
    }

    @Test
    void search_delegatesToRepository() {
        Book book = Book.builder().id(1L).title("T").author("A").difficulty(Difficulty.MEDIUM).build();
        when(bookRepository.search("T", null, null, null)).thenReturn(List.of(book));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("ok");
        List<Book> result = bookService.search("T", null, null, null);
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void updateCategories_clearsAndAdds() {
        Book book = Book.builder().id(1L).title("T").author("A").difficulty(Difficulty.EASY).build();
        book.addCategory("OLD");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("ok");
        bookService.updateCategories(1L, Set.of("ADVENTURE", "FICTION"));
        verify(bookRepository).save(book);
        assertEquals(Set.of("ADVENTURE", "FICTION"), book.getCategories());
    }
}
