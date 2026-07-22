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