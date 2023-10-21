-- INSERT MEMBER
INSERT INTO Member (id, nickname, email, userName, imageNum, intro, scoville, level, socialType, socialId, createdDate, modifiedDate) VALUES (1, '민경혁', 'minkh@naver.com', '경혁', 2, '안녕하세요', 3500, 5, null, null, now(), now());
INSERT INTO Member (id, nickname, email, userName, imageNum, intro, scoville, level, socialType, socialId, createdDate, modifiedDate) VALUES (2, '김서린', 'seorin@naver.com', '서린', 3, '안녕하세용', 4000, 6, null, null, now(), now());

INSERT INTO Food (id, name, image, level, scoville) VALUES (1, '신라면', 'https://mapdagu.s3.ap-northeast-2.amazonaws.com/%EC%8B%A0%EB%9D%BC%EB%A9%B4.png', 5, 3400);
INSERT INTO Food (id, name, image, level, scoville) VALUES (2, '진라면', 'https://mapdagu.s3.ap-northeast-2.amazonaws.com/%EC%A7%84%EB%9D%BC%EB%A9%B4.png', 2, 1270);
INSERT INTO Food (id, name, image, level, scoville) VALUES (3, '너구리', 'https://mapdagu.s3.ap-northeast-2.amazonaws.com/%EB%84%88%EA%B5%AC%EB%A6%AC.jpg', 3, 2300);
INSERT INTO Food (id, name, image, level, scoville) VALUES (4, '불닭볶음면', 'https://mapdagu.s3.ap-northeast-2.amazonaws.com/%EB%B6%88%EB%8B%AD%EB%B3%B6%EC%9D%8C%EB%A9%B4.jpg', 6, 4404);
INSERT INTO Food (id, name, image, level, scoville) VALUES (5, '삼양라면', 'https://mapdagu.s3.ap-northeast-2.amazonaws.com/%EC%82%BC%EC%96%91%EB%9D%BC%EB%A9%B4.jpg', 5, 3000);