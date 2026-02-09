package com.pictet.adventurebook.repository;

import com.pictet.adventurebook.domain.Book;
import com.pictet.adventurebook.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByBookAndSectionId(Book book, Integer sectionId);
}
