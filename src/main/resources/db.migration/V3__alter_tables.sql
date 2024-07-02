CREATE SEQUENCE IF NOT EXISTS notifications_id_seq;

ALTER TABLE notifications
    ALTER COLUMN id SET DEFAULT nextval('notifications_id_seq');

SELECT setval('notifications_id_seq', (SELECT MAX(id) FROM notifications));
