SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE line;
ALTER TABLE line ALTER COLUMN id RESTART WITH 1;
TRUNCATE TABLE station;
ALTER TABLE station ALTER COLUMN id RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;