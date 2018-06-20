/* Populating database with dummy data */
INSERT INTO Doctor (ID, TYPE, NAME) VALUES
(1, 'Dentist', 'Dr. Evil'),
(2, 'GP', 'Dr. Who'),
(3, 'GP', 'Dr. Lecter'),
(4, 'Dentist', 'Dr. Strange');

INSERT INTO Visit (ID, DOCTOR_ID, VISIT_TIME) VALUES
(1, 1, '2018-05-06'),
(2, 2, '2018-06-01');