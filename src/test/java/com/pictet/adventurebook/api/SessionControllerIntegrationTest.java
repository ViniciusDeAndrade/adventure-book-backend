package com.pictet.adventurebook.api;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Consequence;
import com.pictet.adventurebook.domain.ConsequenceType;
import com.pictet.adventurebook.domain.Difficulty;
import com.pictet.adventurebook.domain.Section;
import com.pictet.adventurebook.domain.SectionOption;
import com.pictet.adventurebook.domain.SectionType;
import com.pictet.adventurebook.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerIntegrationTest {

    private static final Long BOOK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void create_returns201AndSession() throws Exception {
        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":" + BOOK_ID + "}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/sessions/")))
                .andExpect(jsonPath("$.bookId", is(BOOK_ID.intValue())))
                .andExpect(jsonPath("$.health", is(10)))
                .andExpect(jsonPath("$.currentSectionId", is(1)));
    }

    @Test
    void getById_returnsSession() throws Exception {
        String location = mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":" + BOOK_ID + "}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(get("/api/sessions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.health", is(10)))
                .andExpect(jsonPath("$.currentSectionId", is(1)));
    }

    @Test
    void getCurrent_returnsBeginSection() throws Exception {
        String location = mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":" + BOOK_ID + "}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(get("/api/sessions/{id}/current", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sectionId", is(1)))
                .andExpect(jsonPath("$.type", is("BEGIN")))
                .andExpect(jsonPath("$.options", hasSize(1)));
    }

    @Test
    void makeChoice_advancesToNextSection() throws Exception {
        String location = mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":" + BOOK_ID + "}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(post("/api/sessions/{id}/choices", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gotoSectionId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sectionId", is(2)))
                .andExpect(jsonPath("$.type", is("END")));
        mockMvc.perform(get("/api/sessions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentSectionId", is(2)))
                .andExpect(jsonPath("$.health", is(10)));
    }

    @Test
    @Transactional
    void makeChoice_withConsequence_reducesHealth() throws Exception {
        Book book = createBookWithConsequence();
        book = bookRepository.save(book);
        Long bookId = book.getId();
        String location = mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":" + bookId + "}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(post("/api/sessions/{id}/choices", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gotoSectionId\":2}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/sessions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.health", is(7)));
    }

    @Test
    void create_invalidBookId_returns404() throws Exception {
        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":99999}"))
                .andExpect(status().isNotFound());
    }

    private Book createBookWithConsequence() {
        Book book = Book.builder()
                .id(3L)
                .title("Consequence Book")
                .author("Author")
                .difficulty(Difficulty.HARD)
                .categories(Set.of())
                .sections(new ArrayList<>())
                .build();
        Section begin = Section.builder().book(book).sectionId(1).text("Start").type(SectionType.BEGIN).options(new ArrayList<>()).build();
        begin.getOptions().add(SectionOption.builder()
                .description("Lose 3 HP")
                .gotoSectionId(2)
                .section(begin)
                .consequence(Consequence.builder().type(ConsequenceType.LOSE_HEALTH).value(3).text("Hurt").build())
                .build());
        Section end = Section.builder().book(book).sectionId(2).text("End").type(SectionType.END).options(new ArrayList<>()).build();
        book.getSections().addAll(List.of(begin, end));
        return book;
    }
}
