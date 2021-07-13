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

CREATE TABLE boardreply (
  id INT AUTO_INCREMENT PRIMARY KEY,
  writer VARCHAR(50) NOT NULL,
  content VARCHAR(200) NOT NULL,
  writedate TIMESTAMP DEFAULT NOW(),
  linkedarticlenum INT(11)
);


INSERT INTO board (boardname, nickname, title, content)VALUES
  ('자유게시판','admin','공지사항','공지사항입니다. 다양한 테스트를 시도해 보십시오.'),
  ('자유게시판','manager','매니저입니다', '새로 매니저가 되었습니다. 잘부탁드립니다.'),
  ('자유게시판','user','사용자입니다','사용자로 가입했습니다. 안녕하세요.'),
  ('질문게시판','tester','테스터입니다', '테스트해보세요'),
  ('질문게시판','tester','테스터입니다', '테스트해보세요'),
  ('질문게시판','tester','테스터입니다', '테스트해보세요');

    
  INSERT INTO commentstbl (writer, content, linkedarticlenum)VALUES
  ('admin','확인했습니다.',1),
  ('user','잘봤습니다.',3);