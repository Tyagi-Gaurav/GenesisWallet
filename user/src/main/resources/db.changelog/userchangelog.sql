--liquibase formatted sql

--changeset user:1
create SCHEMA IF NOT EXISTS USER_SCHEMA;

create TABLE IF NOT EXISTS USER_SCHEMA.USER_TABLE (
    ID VARCHAR(40) NOT NULL,
    USER_NAME VARCHAR(40) UNIQUE NOT NULL,
    FIRST_NAME VARCHAR(256) NOT NULL,
    LAST_NAME VARCHAR(256) NOT NULL,
    PASSWORD VARCHAR(256) NOT NULL,
    DATE_OF_BIRTH VARCHAR(10) NOT NULL,
    GENDER VARCHAR(20) NOT NULL,
    HOME_COUNTRY VARCHAR(3) NOT NULL,
    ROLE VARCHAR(256) NOT NULL,
    PRIMARY KEY (id)
) ;

--changeset user:2
DELETE FROM USER_SCHEMA.USER_TABLE;

--changeset user:3
INSERT INTO USER_SCHEMA.USER_TABLE (ID, USER_NAME, FIRST_NAME, LAST_NAME, PASSWORD, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) values ('dcba7802-2eae-42b2-818e-a27f8f380088', 'admin', 'Gaurav',
'Tyagi', '61646d696e313233', '01/01/1980', 'MALE', 'GBR', 'ADMIN');

--changeset user:4
create TABLE IF NOT EXISTS USER_SCHEMA.EVENTS (
    ID VARCHAR(40) NOT NULL,
    OWNER_USER VARCHAR(40) NOT NULL,
    ORIGINATOR_USER VARCHAR(40) NOT NULL,
    TYPE VARCHAR(256) NOT NULL,
    PAYLOAD JSONB NOT NULL,
    CREATION_TIMESTAMP BIGINT NOT NULL,
    PRIMARY KEY (id)
);