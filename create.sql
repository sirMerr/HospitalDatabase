DROP TABLE IF EXISTS cs1434872.procedure_logs;
DROP TABLE IF EXISTS cs1434872.admin_logs;
DROP TABLE IF EXISTS cs1434872.new_user_logs;
DROP TABLE IF EXISTS cs1434872.raise_logs;

DROP TABLE IF EXISTS cs1434872.invoices;
DROP TABLE IF EXISTS cs1434872.procedures;
DROP TABLE IF EXISTS cs1434872.prescriptions;
DROP TABLE IF EXISTS cs1434872.medications;
DROP TABLE IF EXISTS cs1434872.appointments;
DROP TABLE IF EXISTS cs1434872.waitlist;
DROP TABLE IF EXISTS cs1434872.users;

CREATE TABLE cs1434872.users (
	 user_id  INT PRIMARY KEY AUTO_INCREMENT,
	 username  VARCHAR(50) UNIQUE NOT NULL,
	 password  VARCHAR(30),
     firstname  VARCHAR(45) NOT NULL,
	 lastname  VARCHAR(45) NOT NULL,
	 email  VARCHAR(255) NOT NULL,
     phone  VARCHAR(10) NOT NULL,
     salary  FLOAT,
     notes  VARCHAR(255),
     type  ENUM('Patient', 'Doctor', 'Admin') NOT NULL
);

INSERT INTO cs1434872.users VALUES (null, 'admin', 'password', 'admin', 'admini', 'admin@gmail.com','5148653644', '9001', null, 'Admin');
INSERT INTO cs1434872.users VALUES (null, 'sirmerr', 'password', 'Tiffany', 'Le-Nguyen', 'letiff@gmail.com','5143333544', null, 'is a girl', 'Patient');
INSERT INTO cs1434872.users VALUES (null, 'patient', 'password', 'Patient', 'LePatient', 'patient@gmail.com','5143323544', null, null, 'Patient');
INSERT INTO cs1434872.users VALUES (null, 'doctor', 'password', 'Doc', 'LeDoc', 'doc@gmail.com','2223333544', '1000', null, 'Doctor');
INSERT INTO cs1434872.users VALUES (null, 'doctor1', 'password', 'Doc1', 'LeDoc1', 'doc1@gmail.com','1223333544', '1001', null, 'Doctor');
INSERT INTO cs1434872.users VALUES (null, 'doctor3', 'password', 'Doc3', 'LeDoc3', 'doc3@gmail.com','2223333533', '1003', null, 'Doctor');
INSERT INTO cs1434872.users VALUES (null, 'doctor2', 'password', 'Doc2', 'LeDoc2', 'doc2@gmail.com','1223333522', '1002', null, 'Doctor');
CREATE TABLE  cs1434872.waitlist  (
	 patient_id  INT,
     specialist_id  INT,
	FOREIGN KEY ( patient_id )
		REFERENCES  cs1434872.users ( user_id ),
	FOREIGN KEY ( specialist_id )
		REFERENCES  cs1434872.users ( user_id )
);

CREATE TABLE  cs1434872.appointments  (
	 appointment_id  INT PRIMARY KEY AUTO_INCREMENT,
     patient_id  INT,
     doctor_id  INT,
     date  DATE NOT NULL,
     notes  VARCHAR(255),
    FOREIGN KEY ( patient_id )
		REFERENCES  cs1434872.users ( user_id ),
    FOREIGN KEY ( doctor_id )
		REFERENCES  cs1434872.users ( user_id )
);

CREATE TABLE  cs1434872.medications  (
	 medication_id  INT PRIMARY KEY AUTO_INCREMENT,
     name  VARCHAR(255) NOT NULL,
     price  FLOAT NOT NULL,
     instock  INT NOT NULL
);

CREATE TABLE  cs1434872.prescriptions  (
	 prescription_id  INT PRIMARY KEY AUTO_INCREMENT,
	 patient_id  INT,
     medication_id  INT,
    FOREIGN KEY ( patient_id )
		REFERENCES  cs1434872.users ( user_id ),
    FOREIGN KEY ( medication_id )
		REFERENCES  cs1434872.medications ( medication_id )
);

CREATE TABLE  cs1434872.procedures  (
	 procedure_id  INT PRIMARY KEY AUTO_INCREMENT,
     name  VARCHAR(255) NOT NULL,
     price  FLOAT NOT NULL,
     patient_id  INT,
     doctor_id  INT,
	FOREIGN KEY ( patient_id )
		REFERENCES  cs1434872.users ( user_id ),
	FOREIGN KEY ( doctor_id )
		REFERENCES  cs1434872.users ( user_id )
);

CREATE TABLE  cs1434872.invoices  (
	 invoice_id  INT PRIMARY KEY AUTO_INCREMENT,
     patient_id  INT,
     amount  FLOAT NOT NULL,
    FOREIGN KEY ( patient_id )
		REFERENCES  cs1434872.users ( user_id )
);

CREATE TABLE  cs1434872.new_user_logs  (
	 log_id  INT PRIMARY KEY AUTO_INCREMENT,
	 date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     content  VARCHAR(255) NOT NULL
);

CREATE TABLE  cs1434872.raise_logs  (
	 log_id  INT PRIMARY KEY AUTO_INCREMENT,
	 date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     content  VARCHAR(255) NOT NULL
);

CREATE TABLE  cs1434872.admin_logs  (
	 log_id  INT PRIMARY KEY AUTO_INCREMENT,
	 date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     content  VARCHAR(255) NOT NULL
);

CREATE TABLE  cs1434872.procedure_logs  (
	 notification_id  INT PRIMARY KEY AUTO_INCREMENT,
	 date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     content  VARCHAR(255) NOT NULL
);

CREATE INDEX  index_users_username  ON  cs1434872.users  ( username );
CREATE INDEX  index_users_type  ON  cs1434872.users  ( type );

CREATE INDEX  index_appointments_patient_id  ON  cs1434872.appointments  ( patient_id );
CREATE INDEX  index_appointments_doctor_id  ON  cs1434872.appointments  ( doctor_id );

COMMIT;

INSERT INTO cs1434872.medications (name, price, instock) VALUES
	('Liquid Nitrogen', '99.99', 50),
	('Tylenol', '55.02', 4),
	('Psyche', '2.40', 14),
	('Birth Control Pill', '30', 0);

INSERT INTO cs1434872.procedures (name, price, patient_id, doctor_id) VALUES
	('Soul Cleansing', '505.55','1', '2'),
	('Brain Probing', '27.99', '2', '3'),
	('Stomach Cleaning', '202.22', '2','2'),
	('Pimple Popping', '666.66', '2','1');


INSERT INTO cs1434872.waitlist VALUES
		(1, 1),
    (2, 2);

INSERT INTO cs1434872.appointments (patient_id, doctor_id, date) VALUES
		(1, 2, '2016-01-16'),
    (2, 3, '2016-01-20'),
    (2, 1, '2016-02-09');

INSERT INTO cs1434872.prescriptions (patient_id, medication_id) VALUES
	(1, 1),
	(2, 1),
	(2, 2);
