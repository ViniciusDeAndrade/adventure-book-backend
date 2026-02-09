package com.pictet.adventurebook.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "section_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Integer gotoSectionId;
    @Embedded
    private Consequence consequence;
    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;
}
