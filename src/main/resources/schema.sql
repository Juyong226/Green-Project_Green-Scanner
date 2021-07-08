DROP TABLE IF EXISTS userstbl;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS boardreply;

CREATE TABLE board (
  postno INT AUTO_INCREMENT PRIMARY KEY,
  parentno INT,
  boardname VARCHAR(50) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  id VARCHAR(50),
  title VARCHAR(150) NOT NULL,
  content VARCHAR(1000) NOT NULL,
  postdate TIMESTAMP DEFAULT NOW(),
  attachedfile VARCHAR(100),
  reply_cnt INT(11)
);
 
CREATE TABLE userstbl (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  userid VARCHAR(50) NOT NULL,
  userpw VARCHAR(50) NOT NULL,
  username VARCHAR(50) NOT NULL
);

CREATE TABLE boardreply (
  id INT AUTO_INCREMENT PRIMARY KEY,
  writer VARCHAR(50) NOT NULL,
  content VARCHAR(200) NOT NULL,
  writedate TIMESTAMP DEFAULT NOW(),
  linkedarticlenum INT(11)
);

 
