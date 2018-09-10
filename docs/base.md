# Base preparation

To make the applications available for all test users and their actions let's set permissions for users
and states for applications. The best way - to put all the queries in bash-script.

SELECT queries are for debug.

## Active users

Add permisions for active users:

```bash
DELETE FROM users_roles where uid IN (171, 13815, 9913, 6220, 626);

INSERT INTO users_roles(uid, rid) VALUES
('171', '8'), ('171', '12'), ('171', '13'), ('171', '14'), ('171', '15'), ('171', '16'), ('171', '17'), ('171', '18'),
('171', '20'), ('171', '27'), ('13815', '8'), ('13815', '12'), ('13815', '13'), ('13815', '14'), ('13815', '15'),
('13815', '16'), ('13815', '17'), ('13815', '18'), ('13815', '20'), ('13815', '27'), ('9913', '8'), ('9913', '12'),
('9913', '13'), ('9913', '14'), ('9913', '15'), ('9913', '16'), ('9913', '17'), ('9913', '18'), ('9913', '20'),
('9913', '27'), ('6220', '8'), ('6220', '12'), ('6220', '13'), ('6220', '14'), ('6220', '15'), ('6220', '16'),
('6220', '17'), ('6220', '18'), ('6220', '20'), ('6220', '27'), ('626', '8'), ('626', '12'), ('626', '13'),
('626', '14'), ('626', '15'), ('626', '16'), ('626', '17'), ('626', '18'), ('626', '20'), ('626', '27');
```
Remove fixed users from applications to make applications available for all of them:
```bash
UPDATE table_main tm LEFT JOIN table_status ts ON tm.id = ts.id SET tm.user = '0' WHERE tm.status IN
('a', 'b', 'c');
```
Put 1000 applications on every state:

```bash
UPDATE table_main
SET status = CASE
WHEN id > 50000 AND id <  51000 AND status != 'd' THEN 'a'
WHEN id > 51000 AND id <  52000 AND status != 'd' THEN 'b'
WHEN id > 52000 AND id <  53000 AND status != 'd' THEN 'c'
ELSE status
END
WHERE id IS NOT NULL;
```

## Client group users

Get most active clients ids:
```bash
SELECT client, COUNT(uclient) AS q_client, status FROM `table_main` GROUP BY client, status HAVING client > 100
AND status != 'd' ORDER BY `q_client` DESC LIMIT 10
```
Check how many applications do they have:
```bash
SELECT  COUNT(id), status, client FROM table_main WHERE client IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
AND status IN ('a', 'b', 'c') GROUP BY status, client ORDER BY COUNT(id) DESC
```

## Support

Get the most active support users:

```bash
SELECT support, client, COUNT(id) FROM `table_main` WHERE client IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
GROUP BY support, client ORDER BY COUNT(id) DESC
```
Put some application in state 'in work' for support:

```bash
UPDATE table_main SET status = 'in_work' WHERE id in (SELECT id FROM (SELECT * FROM table_main) AS connection WHERE
support IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10) and client NOT IN
(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) LIMIT 400;
```

## Con-module

Put test applications in con-module:

```bash
UPDATE table_main AS tm INNER JOIN (SELECT dt.key, tm.id FROM data_table AS dt INNER JOIN table_main AS
tm ON tm.id= dt.id WHERE tm.status = 'b' AND dt.key = '' AND dt.substatus = 'editor' AND tm.id BETWEEN
60000 AND 100000 LIMIT 200) AS alz ON tm.id = tm.id SET tm.status = 'c';

```
