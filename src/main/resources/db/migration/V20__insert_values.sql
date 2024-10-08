INSERT INTO Client (client_id, last_name, first_name)
SELECT 'f5b2f891-330b-4196-be78-e6d16004316d', 'Last', 'First'
WHERE NOT EXISTS (SELECT * FROM Client);

INSERT INTO Account (account_id, balance, pin_code, client_id)
SELECT 'c1e7fa46-05b8-4ba5-8de3-7d20fa712f9b', '0.0', '1234', 'f5b2f891-330b-4196-be78-e6d16004316d'
WHERE NOT EXISTS (SELECT * FROM Account);

--INSERT INTO Account (account_id, balance, pin_code, client_id)
--VALUES ('c1e7fa46-05b8-4ba5-8de3-7d20fa712f9b', '0.0', '1234', 'f5b2f891-330b-4196-be78-e6d16004316d')