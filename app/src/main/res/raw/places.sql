DROP TABLE place;

CREATE TABLE place (
   name TEXT PRIMARY KEY,
   description TEXT,
   category TEXT,
   addressTitle TEXT,
   address TEXT,
   elevation NUMBER,
   latitude NUMBER, 
   longitude NUMBER);

INSERT INTO place VALUES
	('ASU-West','Home of ASU''s Applied Computing Program','School','ASU West Campus', '13591 N 47th Ave$Phoenix AZ 85051','1100.0','33.608979','-112.159469');

INSERT INTO place VALUES
	('UAK-Anchorage','University of Alaska''s largest campus','School','University of Alaska at Anchorage', '290 Spirit Dr$Anchorage AK 99508','0.0','61.189748','-149.826721');

INSERT INTO place VALUES
	('Toreros','The University of San Diego, a private Catholic undergraduate university.','School','University of San Diego', '5998 Alcala Park$San Diego CA 92110','200.0','32.771923','-117.188204');

INSERT INTO place VALUES
	('Barrow-Alaska','The only real way in and out of Barrow Alaska.','Travel','Will Rogers Airport', '1725 Ahkovak St$Barrow AK 99723','5.0','71.287881','-156.779417');
