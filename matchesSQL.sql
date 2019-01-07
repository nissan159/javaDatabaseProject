#Nissan Malko
#Matchmaking Database
create database nmalko_db;
GRANT ALL PRIVILEGES ON nmalko_db.* TO 'student'@'localhost';
FLUSH PRIVILEGES;
use nmalko_db;

#This table will hold all of the user's information
create table users(
userID varchar(15),  
pass varChar(50), 
age int, 
sex varchar(15), 
phoneNumber varchar(15), 
hobbies varchar(30),
lookingForRelationship int,
primary key (userID)
);

#This table will hold all the matches
create table matches(
userID varchar(15),
matchID varchar(15),
lookingForRelationship int
);

#This trigger will match users who have the same hobby if they're both looking for a relationship
DELIMITER //
CREATE TRIGGER tg_matches 
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    SET @user = new.userID;
    SET @userHobbies = new.hobbies;
    
    insert into matches (userID, matchID, lookingForRelationship)
	select new.userID, u.userID, TRUE
	from users as u	where 
			new.userID != u.userID and
			u.hobbies = new.hobbies and
            u.lookingForRelationship = 1 and
            new.lookingForRelationship = 1;
END; //
DELIMITER ;

#This trigger will delete any matches if a user in the matches were deleted from the database
DELIMITER //
CREATE TRIGGER tg_delMatches 
AFTER DELETE ON users
FOR EACH ROW
BEGIN
    delete from matches where userID = old.userID;
    delete from matches where matchID = old.userID;
END; //
DELIMITER ;

insert into users values('Kate', SHA('jdoe'), '23', 'female', '847-222-3333', 'databaseDesign', 1); 
insert into users values('jdoe', SHA('jdoe'), '23', 'Male', '847-222-1111', 'databaseDesign', 1); 


