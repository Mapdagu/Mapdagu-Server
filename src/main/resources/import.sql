-- INSERT MEMBER
INSERT INTO Member (id, nickname, email, userName, imageNum, intro, scoville, level, socialType, socialId, createdDate, modifiedDate) VALUES (1, '민경혁', 'minkh@naver.com', '경혁', 2, '안녕하세요', 3500, 5, null, null, now(), now());
INSERT INTO Member (id, nickname, email, userName, imageNum, intro, scoville, level, socialType, socialId, createdDate, modifiedDate) VALUES (2, '김서린', 'seorin@naver.com', '서린', 3, '안녕하세용', 4000, 6, null, null, now(), now());

INSERT INTO Food (id, name, imageNum, level, scoville) VALUES (1, '신라면', 1, 5, 3400);
INSERT INTO Food (id, name, imageNum, level, scoville) VALUES (2, '진라면', 2, 2, 1270);
INSERT INTO Food (id, name, imageNum, level, scoville) VALUES (3, '너구리', 3, 3, 2300);
INSERT INTO Food (id, name, imageNum, level, scoville) VALUES (4, '불닭볶음면', 4, 6, 4404);
INSERT INTO Food (id, name, imageNum, level, scoville) VALUES (5, '삼양라면', 5, 5, 3000);