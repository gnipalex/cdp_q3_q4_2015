INSERT INTO userProfile(id,userName,name,dateOfBirth) 
	VALUES (1,'alex','Alex','1993-04-06');
INSERT INTO userProfile(id,userName,name,dateOfBirth) 
	VALUES (2,'max','Maxim','1990-02-01');
INSERT INTO userProfile(id,userName,name,dateOfBirth) 
	VALUES (3,'igor','Igor','1995-12-13');

INSERT INTO note(id,noteText,postDateTime,authorId,timelineOwnerId) 
	VALUES (1, 'this is first note!', '2012-12-12 11:21:13', 1, 1);
INSERT INTO note(id,noteText,postDateTime,authorId,timelineOwnerId) 
	VALUES (2, 'Did not expect to meet you here! How are you?', '2014-05-23 13:00:00', 2, 1);

INSERT INTO friendship(userId, otherUserId) VALUES (1,2);
INSERT INTO friendship(userId, otherUserId) VALUES (2,3);
INSERT INTO friendship(userId, otherUserId) VALUES (1,3);
