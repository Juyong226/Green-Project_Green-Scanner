
INSERT INTO board (boardname, nickname, title, content)VALUES
  ('자유게시판','admin','공지사항','공지사항입니다. 다양한 테스트를 시도해 보십시오.'),
  ('자유게시판','manager','매니저입니다', '새로 매니저가 되었습니다. 잘부탁드립니다.'),
  ('자유게시판','user','사용자입니다','사용자로 가입했습니다. 안녕하세요.'),
  ('질문게시판','tester','테스터입니다', '테스트해보세요'),
  ('질문게시판','tester','테스터입니다', '테스트해보세요'),
  ('질문게시판','tester','테스터입니다', '테스트해보세요');
  
  INSERT INTO userstbl (userid, userpw, username)VALUES
  ('admin','admin1234','관리자'),
  ('manager','manager1234', '매니저'),
  ('user','user1234','사용자'),
  ('test','test1234', '테스터'),
  ('secure','secure1234', '보안'),
  ('openeg','openeg1234', '오픈이지'),
  ('ceo','ceo1234', '대표이사'),
  ('cto','cto1234', '기술이사');

    
  INSERT INTO commentstbl (writer, content, linkedarticlenum)VALUES
  ('admin','확인했습니다.',1),
  ('user','잘봤습니다.',3);