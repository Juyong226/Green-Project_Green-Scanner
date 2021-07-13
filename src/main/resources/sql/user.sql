DROP TABLE IF EXISTS userstbl;

CREATE TABLE userstbl (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  userid VARCHAR(50) NOT NULL,
  userpw VARCHAR(50) NOT NULL,
  username VARCHAR(50) NOT NULL
);

  INSERT INTO userstbl (userid, userpw, username)VALUES
  ('admin','admin1234','관리자'),
  ('manager','manager1234', '매니저'),
  ('user','user1234','사용자'),
  ('test','test1234', '테스터'),
  ('secure','secure1234', '보안'),
  ('openeg','openeg1234', '오픈이지'),
  ('ceo','ceo1234', '대표이사'),
  ('cto','cto1234', '기술이사');