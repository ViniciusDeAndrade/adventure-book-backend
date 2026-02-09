package com.pictet.adventurebook.repository;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.categories c WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:category IS NULL OR :category IN (SELECT cat FROM b.categories cat)) AND " +
            "(:difficulty IS NULL OR b.difficulty = :difficulty)")
    List<Book> search(@Param("title") String title, @Param("author") String author,
                      @Param("category") String category, @Param("difficulty") Difficulty difficulty);
}
