-- Create tablespace for the application
CREATE TABLESPACE employee_records_ts
DATAFILE 'employee_records_01.dbf'
SIZE 100M
AUTOEXTEND ON;

-- Create user for the application
CREATE USER employee_app IDENTIFIED BY "employee_password"
DEFAULT TABLESPACE employee_records_ts
QUOTA UNLIMITED ON employee_records_ts;

-- Grant necessary privileges
GRANT CREATE SESSION TO employee_app;
GRANT CREATE TABLE TO employee_app;
GRANT CREATE VIEW TO employee_app;
GRANT CREATE SEQUENCE TO employee_app;
GRANT CREATE PROCEDURE TO employee_app;
GRANT CREATE TRIGGER TO employee_app;

-- Connect as the new user
CONNECT employee_app/employee_password;

-- Create sequences
CREATE SEQUENCE employee_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE department_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE audit_seq START WITH 1 INCREMENT BY 1;
