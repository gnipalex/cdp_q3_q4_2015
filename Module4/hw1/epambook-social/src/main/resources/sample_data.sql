INSERT INTO userProfile(id,userName,name,dateOfBirth) 
VALUES (1,'alex','Alex','04/06/1993'), (2,'max','Maxim','01/02/1990'), (3,'igor','Igor','12/13/1995');

INSERT INTO note(id,noteText,postDateTime,authorId,timelineOwnerId) 
VALUES (1, 'this is first note!', '12/12/2012', 1, 1), (2, 'Did not expect to meet you here! How are you?', '05/23/2014', 2, 1);

INSERT INTO friendship(userId, otherUserId) 
VALUES (1,2), (2,3), (1,3);
