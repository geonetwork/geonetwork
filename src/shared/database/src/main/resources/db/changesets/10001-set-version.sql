--liquibase formatted sql
--changeset francois:10001
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM settings WHERE name='system/platform/version' AND value='4.4.10';

UPDATE Settings SET value='5.0.0' WHERE name='system/platform/version';
