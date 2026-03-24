CREATE TABLE IF NOT EXISTS students (
    student_id  VARCHAR(20) PRIMARY KEY,
    faculty     VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    first_name  VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100) NOT NULL,
    photo_url   VARCHAR(500)
);

INSERT INTO students (student_id, faculty, last_name, first_name, middle_name, photo_url) VALUES
    ('ЗК-2021-001', 'Факультет информационных технологий', 'Иванов',   'Алексей',  'Сергеевич',   'photos/ivanov.jpg'),
    ('ЗК-2021-002', 'Факультет математики и физики',       'Петрова',  'Мария',    'Александровна', 'photos/petrova.jpg'),
    ('ЗК-2022-003', 'Факультет экономики и управления',    'Сидоров',  'Дмитрий',  'Владимирович',  NULL)
ON CONFLICT (student_id) DO NOTHING;
