INSERT INTO book (id, title, author, difficulty) VALUES
                                                     (1, 'Integration Book', 'Test Author', 'EASY'),
                                                     (2, 'Second Book', 'Author B', 'MEDIUM');

INSERT INTO section (id, section_id, text, type, book_id) VALUES
                                                              (1, 1, 'Start', 'BEGIN', 1),
                                                              (2, 2, 'End', 'END', 1),
                                                              (3, 1, 'Start B', 'BEGIN', 2),
                                                              (4, 2, 'End B', 'END', 2);

INSERT INTO section_option (id, description, goto_section_id, section_id) VALUES
                                                                              (1, 'Go to end', 2, 1),
                                                                              (2, 'To end B', 4, 3);

-- 🔴 ESSENCIAL: avançar os AUTO_INCREMENT
ALTER TABLE book ALTER COLUMN id RESTART WITH 100;
ALTER TABLE section ALTER COLUMN id RESTART WITH 100;
ALTER TABLE section_option ALTER COLUMN id RESTART WITH 100;
ALTER TABLE game_session ALTER COLUMN id RESTART WITH 100;
