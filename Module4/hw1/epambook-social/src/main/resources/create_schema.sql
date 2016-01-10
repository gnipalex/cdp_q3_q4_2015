CREATE TABLE userProfile(
	id bigint IDENTITY primary key,
	userName varchar(50) not null,
	name varchar(50) not null,
	dateOfBirth date not null,
	
	constraint unique_userName unique (userName)
);

CREATE TABLE note(
	id bigint IDENTITY primary key,
	noteText varchar(500) not null,
	postDateTime datetime not null,
	authorId bigint not null,
	timelineOwnerId bigint not null,
	
	constraint fk_note_profile_authorId foreign key (authorId) references profile(id) 
		on delete set null on update cascade,
	constraint fk_note_profile_timelineOwnerId foreign key (timelineOwnerId) references profile(id) 
		on delete cascade on update cascade
);

CREATE TABLE friendship(
	userId bigint not null,
	otherUserId bigint not null,
	
	constraint fk_friendship_profile_userId foreign key (userId) references profile(id) 
		on delete cascade on update cascade,
	constraint fk_friendship_profile_otherUserId foreign key (otherUserId) references profile(id) 
		on delete cascade on update cascade
);

/*
SELECT * FROM profile WHERE id IN (
	SELECT DISTINCT * FROM (
		SELECT userId FROM friendship WHERE otherUserId=?
		UNION 
		SELECT otherUserId FROM friendship WHERE userId=?
	)
)

SELECT count(userId) FROM friendship 
	WHERE (userId=? AND otherUserId=?) OR (userId=? AND otherUserId=?)
*/