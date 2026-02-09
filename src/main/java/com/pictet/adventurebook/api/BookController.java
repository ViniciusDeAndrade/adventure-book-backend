package com.pictet.adventurebook.api;

import com.pictet.adventurebook.api.dto.BookDetailDto;
import com.pictet.adventurebook.api.dto.BookDto;
import com.pictet.adventurebook.api.dto.CategoriesUpdateDto;
import com.pictet.adventurebook.api.dto.SectionReadDto;
import com.pictet.adventurebook.api.mapper.BookMapper;
import com.pictet.adventurebook.api.mapper.SectionMapper;
import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Difficulty;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.exception.ResourceNotFoundException;
import com.pictet.adventurebook.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books")
@Slf4j
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final SectionMapper sectionMapper;
    private final MessageSource messageSource;

    @GetMapping
    @Operation(summary = "List and search books")
    public List<BookDto> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Difficulty difficulty) {
        List<Book> books = bookService.search(title, author, category, difficulty);
        return books.stream().map(bookMapper::toListDto).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book details")
    public ResponseEntity<BookDetailDto> getById(@PathVariable Long id) {
        Book book = bookService.getById(id);
        return ResponseEntity.ok(bookMapper.toDetailDto(book));
    }

    @PatchMapping("/{id}/categories")
    @Operation(summary = "Update book categories")
    public ResponseEntity<BookDetailDto> patchCategories(
            @PathVariable Long id,
            @Valid @RequestBody CategoriesUpdateDto body) {
        bookService.updateCategories(id, body.getCategories());
        return ResponseEntity.ok(bookMapper.toDetailDto(bookService.getById(id)));
    }

    @GetMapping("/{bookId}/sections/{sectionId}")
    @Operation(summary = "Read a section")
    public ResponseEntity<SectionReadDto> getSection(
            @PathVariable Long bookId,
            @PathVariable Integer sectionId) {
        log.debug(messageSource.getMessage("log.book.sectionRequested", new Object[]{bookId, sectionId}, Locale.getDefault()));
        Book book = bookService.getById(bookId);
        Section section = book.getSections().stream()
                .filter(s -> s.getSectionId().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage("error.section.notFound", null, Locale.getDefault());
                    log.warn(messageSource.getMessage("log.exception.notFound", new Object[]{msg}, Locale.getDefault()));
                    return new ResourceNotFoundException(msg);
                });
        return ResponseEntity.ok(sectionMapper.toReadDto(section));
    }
}
