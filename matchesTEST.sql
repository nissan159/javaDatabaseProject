#Nissan Malko
#Name of Project
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
lookingForRelationship boolean,
primary key (userID)
);


create table matches(
userID varchar(15),
matchID varchar(15),
lookingForRelationship boolean);

select hobbies from user;

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
            u.lookingForRelationship = true and
            new.lookingForRelationship = true
	;
END; //
DELIMITER ;

drop trigger tg_matches;
drop table users;

insert into users values('NissanMalko', SHA('password'), '23', 'male', '847-222-3854', 'basketball', TRUE); 
insert into users values('VickySpicky', SHA('password'), '23', 'female', '847-222-3854', 'basketball', TRUE); 
insert into users values('VickySpickyyyy', SHA('password'), '23', 'female', '847-222-3854', 'basketball', TRUE); 

delete from users where userID = 'VickySpickyyyy';

select hobbies from users where hobbies = @userHobbies;

select lookingForRelationship from users where hobbies = (select userID,hobbies from user);
select * from matches;

