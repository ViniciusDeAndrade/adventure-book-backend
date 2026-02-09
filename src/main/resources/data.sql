INSERT INTO book (id, title, author, difficulty) VALUES
(1, 'The Crystal Caverns', 'Evelyn Stormrider', 'EASY'),
(2, 'The Prisoner', 'Daniel El Fuego', 'HARD');

INSERT INTO section (id, section_id, text, type, book_id) VALUES
(1, 1, 'You stand at the entrance of the Crystal Caverns.', 'BEGIN', 1),
(2, 2, 'You reach the end of the cavern.', 'END', 1),
(3, 1, 'You wake up in a dark prison cell.', 'BEGIN', 2),
(4, 2, 'The door opens. You are free.', 'END', 2);

INSERT INTO section_option (id, description, goto_section_id, section_id) VALUES
(1, 'Enter the cavern', 2, 1),
(2, 'Try the key on the door', 4, 3);
