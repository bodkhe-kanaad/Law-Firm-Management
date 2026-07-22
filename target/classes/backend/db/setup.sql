
-- Law Firm Case Management - setup.sql


-- 0. Clean Database Drop child tables before parent tables


DROP TABLE Presented CASCADE CONSTRAINTS;
DROP TABLE Evidence CASCADE CONSTRAINTS;
DROP TABLE Hearing CASCADE CONSTRAINTS;
DROP TABLE Invoice CASCADE CONSTRAINTS;
DROP TABLE WorkLog CASCADE CONSTRAINTS;
DROP TABLE Court_Case CASCADE CONSTRAINTS;
DROP TABLE Corporate CASCADE CONSTRAINTS;
DROP TABLE Criminal CASCADE CONSTRAINTS;
DROP TABLE Family CASCADE CONSTRAINTS;
DROP TABLE Invoice_status CASCADE CONSTRAINTS;
DROP TABLE CourtHouse CASCADE CONSTRAINTS;
DROP TABLE Lawyer CASCADE CONSTRAINTS;
DROP TABLE Client CASCADE CONSTRAINTS;

-- 1. Create Tables

-- Parent Tables

CREATE TABLE Client (
    client_id INTEGER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    contact VARCHAR2(100) NOT NULL,
    amount_payable DECIMAL(10,2)
);

CREATE TABLE Lawyer (
    lawyer_id INTEGER PRIMARY KEY,
    bar_id VARCHAR2(50) UNIQUE NOT NULL,
    name VARCHAR2(100) NOT NULL,
    start_date DATE,
    new_cases INTEGER,
    success_rate DECIMAL(5,2)
);

CREATE TABLE CourtHouse (
    courtid INTEGER PRIMARY KEY,
    judge_name VARCHAR2(100),
    city VARCHAR2(50) NOT NULL,
    province VARCHAR2(50) NOT NULL
);

CREATE TABLE Invoice_status (
    status_code NUMBER(1) PRIMARY KEY,
    status_description VARCHAR2(20)
);

-- Child Tables - Level 1 Dependencies

CREATE TABLE Family (
    lawyer_id INTEGER PRIMARY KEY,
    rate DECIMAL(10,2),
    mediation_certified NUMBER(1),
    FOREIGN KEY (lawyer_id) REFERENCES Lawyer(lawyer_id) ON DELETE CASCADE
);

CREATE TABLE Criminal (
    lawyer_id INTEGER PRIMARY KEY,
    rate DECIMAL(10,2),
    security_clearance VARCHAR2(50),
    FOREIGN KEY (lawyer_id) REFERENCES Lawyer(lawyer_id) ON DELETE CASCADE
);

CREATE TABLE Corporate (
    lawyer_id INTEGER PRIMARY KEY,
    rate DECIMAL(10,2),
    retainer_required NUMBER(1),
    FOREIGN KEY (lawyer_id) REFERENCES Lawyer(lawyer_id) ON DELETE CASCADE
);

CREATE TABLE Court_Case (
    case_id INTEGER PRIMARY KEY,
    opening_date DATE NOT NULL,
    closing_date DATE,
    case_type VARCHAR2(50),
    is_successful NUMBER(1),
    client_id INTEGER NOT NULL,
    lawyer_id INTEGER NOT NULL,
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (lawyer_id) REFERENCES Lawyer(lawyer_id)
);

-- Child Tables - Level 2 Dependencies

CREATE TABLE WorkLog (
    lawyer_id INTEGER NOT NULL,
    case_id INTEGER NOT NULL,
    log_date DATE,
    number_of_hours DECIMAL(5,2),
    PRIMARY KEY (lawyer_id, case_id, log_date),
    FOREIGN KEY (lawyer_id) REFERENCES Lawyer(lawyer_id),
    FOREIGN KEY (case_id) REFERENCES Court_Case(case_id)
);

CREATE TABLE Invoice (
    invoice_id INTEGER PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    case_id INTEGER UNIQUE NOT NULL,
    status_code NUMBER(1) NOT NULL,
    FOREIGN KEY (case_id) REFERENCES Court_Case(case_id),
    FOREIGN KEY (status_code) REFERENCES Invoice_status(status_code)
);

CREATE TABLE Hearing (
    case_id INTEGER,
    hearing_date DATE,
    courtid INTEGER NOT NULL,
    PRIMARY KEY (case_id, hearing_date),
    FOREIGN KEY (case_id) REFERENCES Court_Case(case_id) ON DELETE CASCADE,
    FOREIGN KEY (courtid) REFERENCES CourtHouse(courtid)
);

CREATE TABLE Evidence (
    evidence_id INTEGER PRIMARY KEY,
    stored_in VARCHAR2(50),
    evidence_type VARCHAR2(50),
    case_id INTEGER NOT NULL,
    FOREIGN KEY (case_id) REFERENCES Court_Case(case_id)
);

-- Child Tables - Level 3 Dependencies

CREATE TABLE Presented (
    evidence_id INTEGER,
    case_id INTEGER,
    hearing_date DATE,
    PRIMARY KEY (evidence_id, case_id, hearing_date),
    FOREIGN KEY (evidence_id) REFERENCES Evidence(evidence_id),
    FOREIGN KEY (case_id, hearing_date) REFERENCES Hearing(case_id, hearing_date)
);

-- 2. Insert Parent Data

INSERT INTO Client VALUES (1, 'Alice Smith', 'alice@email.com', 1500.00);
INSERT INTO Client VALUES (2, 'Bob Jones', 'bob@email.com', 0.00);
INSERT INTO Client VALUES (3, 'Charlie Brown', 'charlie@email.com', 450.50);
INSERT INTO Client VALUES (4, 'Diana Prince', 'diana@email.com', 3200.00);
INSERT INTO Client VALUES (5, 'Evan Wright', 'evan@email.com', 100.00);

INSERT INTO Lawyer VALUES (101, 'BAR001', 'Harvey Specter', TO_DATE('2015-01-15', 'YYYY-MM-DD'), 5, 95.5);
INSERT INTO Lawyer VALUES (102, 'BAR002', 'Saul Goodman', TO_DATE('2018-03-22', 'YYYY-MM-DD'), 12, 88.0);
INSERT INTO Lawyer VALUES (103, 'BAR003', 'Kim Wexler', TO_DATE('2016-07-11', 'YYYY-MM-DD'), 3, 92.0);
INSERT INTO Lawyer VALUES (104, 'BAR004', 'Matt Murdock', TO_DATE('2020-09-01', 'YYYY-MM-DD'), 8, 85.5);
INSERT INTO Lawyer VALUES (105, 'BAR005', 'Alicia Florrick', TO_DATE('2012-11-05', 'YYYY-MM-DD'), 2, 98.0);
INSERT INTO Lawyer VALUES (106, 'BAR006', 'James McGill', TO_DATE('2017-05-20', 'YYYY-MM-DD'), 4, 80.0);
INSERT INTO Lawyer VALUES (107, 'BAR007', 'Elle Woods', TO_DATE('2019-08-15', 'YYYY-MM-DD'), 6, 91.0);
INSERT INTO Lawyer VALUES (108, 'BAR008', 'Perry Mason', TO_DATE('2014-03-10', 'YYYY-MM-DD'), 9, 94.0);
INSERT INTO Lawyer VALUES (109, 'BAR009', 'Jack McCoy', TO_DATE('2010-06-01', 'YYYY-MM-DD'), 15, 87.5);
INSERT INTO Lawyer VALUES (110, 'BAR010', 'Olivia Benson', TO_DATE('2013-09-30', 'YYYY-MM-DD'), 11, 89.0);
INSERT INTO Lawyer VALUES (111, 'BAR011', 'Mike Ross', TO_DATE('2021-01-12', 'YYYY-MM-DD'), 2, 76.0);
INSERT INTO Lawyer VALUES (112, 'BAR012', 'Louis Litt', TO_DATE('2016-04-18', 'YYYY-MM-DD'), 7, 83.0);
INSERT INTO Lawyer VALUES (113, 'BAR013', 'Jessica Pearson', TO_DATE('2008-11-22', 'YYYY-MM-DD'), 20, 97.0);
INSERT INTO Lawyer VALUES (114, 'BAR014', 'Robert Zane', TO_DATE('2011-07-14', 'YYYY-MM-DD'), 13, 90.5);
INSERT INTO Lawyer VALUES (115, 'BAR015', 'Rachel Zane', TO_DATE('2022-02-28', 'YYYY-MM-DD'), 1, 78.0);

INSERT INTO CourtHouse VALUES (1, 'Hon. Judy', 'Vancouver', 'BC');
INSERT INTO CourtHouse VALUES (2, 'Hon. Marshall', 'Burnaby', 'BC');
INSERT INTO CourtHouse VALUES (3, 'Hon. Thurgood', 'Richmond', 'BC');
INSERT INTO CourtHouse VALUES (4, 'Hon. Ginsburg', 'Surrey', 'BC');
INSERT INTO CourtHouse VALUES (5, 'Hon. Scalia', 'Victoria', 'BC');

INSERT INTO Invoice_status VALUES (1, 'Pending');
INSERT INTO Invoice_status VALUES (2, 'Paid');
INSERT INTO Invoice_status VALUES (3, 'Overdue');
INSERT INTO Invoice_status VALUES (4, 'Cancelled');
INSERT INTO Invoice_status VALUES (5, 'Refunded');


-- 3. Insert Specialization Data Each lawyer belongs to exactly one specialization

INSERT INTO Corporate VALUES (101, 800.00, 1);
INSERT INTO Criminal VALUES (102, 300.00, 'Level 2');
INSERT INTO Family VALUES (103, 350.00, 1);
INSERT INTO Criminal VALUES (104, 250.00, 'Level 1');
INSERT INTO Family VALUES (105, 400.00, 1);
INSERT INTO Family VALUES (106, 375.00, 0);
INSERT INTO Family VALUES (107, 420.00, 1);
INSERT INTO Family VALUES (108, 390.00, 1);
INSERT INTO Criminal VALUES (109, 275.00, 'Level 3');
INSERT INTO Criminal VALUES (110, 320.00, 'Level 2');
INSERT INTO Criminal VALUES (111, 230.00, 'Level 1');
INSERT INTO Corporate VALUES (112, 750.00, 0);
INSERT INTO Corporate VALUES (113, 950.00, 1);
INSERT INTO Corporate VALUES (114, 880.00, 1);
INSERT INTO Corporate VALUES (115, 700.00, 0);

-- 4. Insert Court_Case Data case_type restricted to Family / Criminal / Corporate

INSERT INTO Court_Case VALUES (1001, TO_DATE('2025-01-10', 'YYYY-MM-DD'), NULL, 'Family', 0, 1, 103);
INSERT INTO Court_Case VALUES (1002, TO_DATE('2025-02-15', 'YYYY-MM-DD'), TO_DATE('2025-03-01', 'YYYY-MM-DD'), 'Criminal', 1, 2, 102);
INSERT INTO Court_Case VALUES (1003, TO_DATE('2024-11-20', 'YYYY-MM-DD'), NULL, 'Corporate', 0, 4, 101);
INSERT INTO Court_Case VALUES (1004, TO_DATE('2025-03-01', 'YYYY-MM-DD'), NULL, 'Criminal', 0, 3, 104);
INSERT INTO Court_Case VALUES (1005, TO_DATE('2024-06-15', 'YYYY-MM-DD'), TO_DATE('2025-01-10', 'YYYY-MM-DD'), 'Family', 1, 5, 105);

INSERT INTO Court_Case VALUES (1006, TO_DATE('2025-04-01', 'YYYY-MM-DD'), NULL, 'Corporate', 0, 1, 101);
INSERT INTO Court_Case VALUES (1007, TO_DATE('2025-05-10', 'YYYY-MM-DD'), NULL, 'Criminal', 0, 2, 102);
INSERT INTO Court_Case VALUES (1008, TO_DATE('2025-06-01', 'YYYY-MM-DD'), NULL, 'Family', 0, 3, 103);
INSERT INTO Court_Case VALUES (1009, TO_DATE('2025-07-15', 'YYYY-MM-DD'), TO_DATE('2025-09-01', 'YYYY-MM-DD'), 'Criminal', 1, 4, 104);
INSERT INTO Court_Case VALUES (1010, TO_DATE('2025-08-20', 'YYYY-MM-DD'), NULL, 'Family', 0, 5, 105);
INSERT INTO Court_Case VALUES (1011, TO_DATE('2025-03-01', 'YYYY-MM-DD'), TO_DATE('2025-06-15', 'YYYY-MM-DD'), 'Corporate', 1, 2, 112);
INSERT INTO Court_Case VALUES (1012, TO_DATE('2025-04-10', 'YYYY-MM-DD'), TO_DATE('2025-08-30', 'YYYY-MM-DD'), 'Family', 1, 3, 106);

-- 5. Insert WorkLog Data

INSERT INTO WorkLog VALUES (103, 1001, TO_DATE('2025-01-11', 'YYYY-MM-DD'), 4.5);
INSERT INTO WorkLog VALUES (102, 1002, TO_DATE('2025-02-16', 'YYYY-MM-DD'), 2.0);
INSERT INTO WorkLog VALUES (101, 1003, TO_DATE('2024-11-21', 'YYYY-MM-DD'), 10.0);
INSERT INTO WorkLog VALUES (104, 1004, TO_DATE('2025-03-02', 'YYYY-MM-DD'), 6.5);
INSERT INTO WorkLog VALUES (105, 1005, TO_DATE('2024-06-16', 'YYYY-MM-DD'), 3.0);

INSERT INTO WorkLog VALUES (101, 1006, TO_DATE('2025-04-02', 'YYYY-MM-DD'), 8.0);
INSERT INTO WorkLog VALUES (101, 1003, TO_DATE('2025-04-10', 'YYYY-MM-DD'), 5.0);
INSERT INTO WorkLog VALUES (102, 1002, TO_DATE('2025-03-05', 'YYYY-MM-DD'), 3.0);
INSERT INTO WorkLog VALUES (103, 1008, TO_DATE('2025-06-02', 'YYYY-MM-DD'), 3.5);
INSERT INTO WorkLog VALUES (103, 1001, TO_DATE('2025-02-15', 'YYYY-MM-DD'), 4.0);
INSERT INTO WorkLog VALUES (104, 1009, TO_DATE('2025-07-16', 'YYYY-MM-DD'), 5.0);
INSERT INTO WorkLog VALUES (105, 1010, TO_DATE('2025-08-21', 'YYYY-MM-DD'), 4.0);

-- 6. Insert Invoice Data Only closed cases get invoices

INSERT INTO Invoice VALUES (5002, 600.00, 1002, 2);
INSERT INTO Invoice VALUES (5005, 1350.00, 1005, 2);
INSERT INTO Invoice VALUES (5009, 875.00, 1009, 2);
INSERT INTO Invoice VALUES (5011, 2200.00, 1011, 2);
INSERT INTO Invoice VALUES (5012, 1100.00, 1012, 1);

-- 7. Insert Hearing Data

INSERT INTO Hearing VALUES (1001, TO_DATE('2025-02-01', 'YYYY-MM-DD'), 1);
INSERT INTO Hearing VALUES (1002, TO_DATE('2025-02-20', 'YYYY-MM-DD'), 2);
INSERT INTO Hearing VALUES (1003, TO_DATE('2025-01-15', 'YYYY-MM-DD'), 1);
INSERT INTO Hearing VALUES (1004, TO_DATE('2025-03-15', 'YYYY-MM-DD'), 3);
INSERT INTO Hearing VALUES (1005, TO_DATE('2024-09-10', 'YYYY-MM-DD'), 4);

INSERT INTO Hearing VALUES (1006, TO_DATE('2025-05-01', 'YYYY-MM-DD'), 2);
INSERT INTO Hearing VALUES (1007, TO_DATE('2025-06-15', 'YYYY-MM-DD'), 3);
INSERT INTO Hearing VALUES (1008, TO_DATE('2025-07-10', 'YYYY-MM-DD'), 4);
INSERT INTO Hearing VALUES (1009, TO_DATE('2025-08-05', 'YYYY-MM-DD'), 5);
INSERT INTO Hearing VALUES (1010, TO_DATE('2025-09-20', 'YYYY-MM-DD'), 1);

-- 8. Insert Evidence Data

INSERT INTO Evidence VALUES (901, 'Locker A', 'Custody Agreement', 1001);
INSERT INTO Evidence VALUES (902, 'Locker B', 'Security Footage', 1002);
INSERT INTO Evidence VALUES (903, 'Server', 'Email Thread', 1003);
INSERT INTO Evidence VALUES (904, 'Locker C', 'Weapon', 1004);
INSERT INTO Evidence VALUES (905, 'Digital', 'Text Messages', 1005);

INSERT INTO Evidence VALUES (906, 'Locker D', 'Contract', 1006);
INSERT INTO Evidence VALUES (907, 'Digital', 'CCTV Footage', 1007);
INSERT INTO Evidence VALUES (908, 'Locker E', 'Birth Certificate', 1008);
INSERT INTO Evidence VALUES (909, 'Server', 'Financial Report', 1009);
INSERT INTO Evidence VALUES (910, 'Digital', 'Voice Recording', 1010);

-- 9. Insert Presented Data

INSERT INTO Presented VALUES (901, 1001, TO_DATE('2025-02-01', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (902, 1002, TO_DATE('2025-02-20', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (903, 1003, TO_DATE('2025-01-15', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (904, 1004, TO_DATE('2025-03-15', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (905, 1005, TO_DATE('2024-09-10', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (906, 1006, TO_DATE('2025-05-01', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (907, 1007, TO_DATE('2025-06-15', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (908, 1008, TO_DATE('2025-07-10', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (909, 1009, TO_DATE('2025-08-05', 'YYYY-MM-DD'));
INSERT INTO Presented VALUES (910, 1010, TO_DATE('2025-09-20', 'YYYY-MM-DD'));

COMMIT;