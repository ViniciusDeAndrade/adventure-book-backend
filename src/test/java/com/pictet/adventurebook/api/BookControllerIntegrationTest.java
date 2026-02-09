package com.pictet.adventurebook.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    private static final Long BOOK_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void list_returnsBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Integration Book")))
                .andExpect(jsonPath("$[0].difficulty", is("EASY")));
    }

    @Test
    void list_filterByTitle_returnsMatching() throws Exception {
        mockMvc.perform(get("/api/books").param("title", "Integration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/api/books").param("title", "Nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getById_returnsBook() throws Exception {
        mockMvc.perform(get("/api/books/{id}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(BOOK_ID.intValue())))
                .andExpect(jsonPath("$.title", is("Integration Book")))
                .andExpect(jsonPath("$.categories", hasSize(0)));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/books/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("not found")));
    }

    @Test
    void patchCategories_updatesAndReturnsBook() throws Exception {
        mockMvc.perform(patch("/api/books/{id}/categories", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categories\":[\"ADVENTURE\",\"FICTION\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(2)))
                .andExpect(jsonPath("$.categories", hasItems("ADVENTURE", "FICTION")));
    }

    @Test
    void getSection_returnsSectionWithOptions() throws Exception {
        mockMvc.perform(get("/api/books/{bookId}/sections/{sectionId}", BOOK_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sectionId", is(1)))
                .andExpect(jsonPath("$.type", is("BEGIN")))
                .andExpect(jsonPath("$.options", hasSize(1)))
                .andExpect(jsonPath("$.options[0].gotoSectionId", is(2)));
    }
}
