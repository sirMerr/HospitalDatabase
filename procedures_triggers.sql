DROP PROCEDURE IF EXISTS cs1434872.make_appt_procedure;
DROP PROCEDURE IF EXISTS cs1434872.cancel_appt_procedure;
DROP PROCEDURE IF EXISTS cs1434872.raise_procedure;
DROP PROCEDURE IF EXISTS cs1434872.procedure_procedure;
DROP PROCEDURE IF EXISTS cs1434872.get_prescriptions_procedure;
DROP PROCEDURE IF EXISTS cs1434872.get_procedures_procedure;
DROP PROCEDURE IF EXISTS cs1434872.waitlist_procedure;
DROP PROCEDURE IF EXISTS cs1434872.get_invoices_procedure;
DROP PROCEDURE IF EXISTS cs1434872.prescribe_medication_procedure;
DROP TRIGGER IF EXISTS cs1434872.after_users_insert_trigger;
DROP TRIGGER IF EXISTS cs1434872.after_users_update;
DROP TRIGGER IF EXISTS cs1434872.after_medication_update_trigger;


DELIMITER //

CREATE PROCEDURE cs1434872.raise_procedure(IN doctor_id INT, IN raise FLOAT)
BEGIN
	DECLARE curr_salary FLOAT;
  DECLARE new_salary FLOAT DEFAULT curr_salary;

  SELECT salary INTO curr_salary
	FROM users WHERE user_id=doctor;

	SET new_salary = curr_salary + raise;

  UPDATE users SET salary=new_salary
	WHERE user_id=doctor_id;
END //

CREATE PROCEDURE cs1434872.make_appt_procedure(IN patient INT, IN doctor INT, IN appointment_date DATE, IN note VARCHAR(255))
BEGIN
	IF appointment_date < CURDATE()
    THEN
		SIGNAL SQLSTATE '45000'
        SET message_text = "Invalid: this date is in the past";
	END IF;

	INSERT INTO cs1434872.appointments (patient_id, doctor_id, date, notes)
		VALUES (patient, doctor, appointment_date, note);
END //

CREATE PROCEDURE cs1434872.cancel_appt_procedure(IN appointment INT)
BEGIN
	DELETE FROM appointments WHERE appointment_id=appointment;
END //

CREATE PROCEDURE cs1434872.get_prescriptions_procedure(IN patient INT)
BEGIN
	SELECT prescription_id, name
		FROM prescriptions
		INNER JOIN medications ON medications.medication_id=prescriptions.medication_id
		WHERE patient_id=patient
		ORDER BY prescription_id;
END //

CREATE PROCEDURE cs1434872.get_procedures_procedure(IN patient INT)
BEGIN
	SELECT procedures.procedure_id, procedure_name FROM procedures
		INNER JOIN procedures_patients ON procedures.procedure_id=procedures_patients.procedure_id
		INNER JOIN users ON patient_id=user_id
		WHERE patient_id=patient
        ORDER BY procedure_id DESC;
END //

CREATE PROCEDURE cs1434872.get_invoices_procedure(IN patient INT)
BEGIN
	SELECT invoice_id, invoice_amount
		FROM invoices
        WHERE patient_id=patient
        ORDER BY invoice_id DESC;
END//

CREATE PROCEDURE cs1434872.waitlist_procedure(IN patient INT)
BEGIN
	SELECT doctor_id, firstname, lastname
		FROM waitlist
		INNER JOIN users ON user_id=doctor_id
		WHERE patient_id=patient;
END //

/* Prescribes a given medication to a given patient */
CREATE PROCEDURE cs1434872.prescribe_medication_procedure(IN patient INT, IN medication INT, IN doctor INT)
BEGIN
	DECLARE medication_instock INT;
    DECLARE medication_cost FLOAT;

    DECLARE p_name VARCHAR(255);
    DECLARE d_name VARCHAR(255);
    DECLARE m_name VARCHAR(255);

    SELECT name, cost, instock INTO m_name, medication_cost, medication_instock
		FROM medications
        WHERE medication_id=medication;

    IF medication_instock = 0
    THEN
		SIGNAL SQLSTATE '45000'
		SET message_text = "No more in stock";
	END IF;

    UPDATE medications SET instock = (medication_instock - 1)
		WHERE medication_id=medication;

    INSERT INTO cs1434872.prescriptions VALUES (null, patient, medication);

    INSERT INTO cs1434872.invoices VALUES (null, patient, medication_cost);

    SELECT CONCAT(firstname, " ", lastname) INTO p_name
		FROM users
        WHERE user_id=patient;

	SELECT CONCAT(firstname, " ", lastname) INTO d_name
		FROM users
        WHERE user_id=doctor;

    INSERT INTO cs1434872.logs_admin (entry) VALUES (CONCAT(m_name, " given to ", p_name, " by ", d_name));
END //

CREATE PROCEDURE cs1434872.procedure_procedure(IN patient INT, IN procedureID INT, IN doctor INT)
BEGIN
	DECLARE procedure_cost INT;

    DECLARE p_name VARCHAR(255);
    DECLARE d_name VARCHAR(255);
    DECLARE pr_name VARCHAR(255);

    SELECT procedure_name, cost INTO pr_name, procedure_cost
		FROM procedures
        WHERE procedure_id=procedureID;

	INSERT INTO cs1434872.procedures_patients VALUES (procedureID, patient);

    INSERT INTO cs1434872.invoices VALUES (null, patient, procedure_cost);

    SELECT CONCAT(firstname, " ", lastname) INTO p_name
		FROM users
        WHERE user_id=patient;

	SELECT CONCAT(firstname, " ", lastname) INTO d_name
		FROM users
        WHERE user_id=doctor;

    INSERT INTO cs1434872.admin_logs (entry) VALUES (CONCAT(pr_name, " performed on ", p_name, " by ", d_name));
END //

CREATE TRIGGER cs1434872.after_users_insert_trigger AFTER INSERT ON users FOR EACH ROW
BEGIN
	INSERT INTO  cs1434872.new_user_logs  (entry) VALUES (CONCAT("User '", NEW.username, "' created"));
END //

CREATE TRIGGER cs1434872.after_users_update AFTER UPDATE ON users FOR EACH ROW
BEGIN
	IF OLD.salary <> NEW.salary
    THEN
		INSERT INTO  cs1434872.raise_logs  (entry) VALUES (CONCAT("Salary of doctor with id ", NEW.user_id, " changed from ", OLD.salary, " to ", NEW.salary));
    END IF;
END //

CREATE TRIGGER cs1434872.after_medication_update_trigger AFTER UPDATE ON medications FOR EACH ROW
BEGIN
	IF NEW.instock < 6 THEN
		INSERT INTO  cs1434872.notifications  (entry) VALUES (CONCAT("Medication ", NEW.name, " stock is ", NEW.instock));
	END IF;
END //


DELIMITER ;
