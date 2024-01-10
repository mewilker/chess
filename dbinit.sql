CREATE DATABASE IF NOT EXISTS chess;
USE chess;
CREATE TABLE  IF NOT EXISTS users (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (username)
);
CREATE TABLE IF NOT EXISTS authTokens(
	username VARCHAR (255) NOT NULL,
    token VARCHAR(255) NOT NULL,
    PRIMARY KEY (token),
    FOREIGN KEY(username) references users(username)
);
CREATE TABLE IF NOT EXISTS games(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    whiteUsername VARCHAR(255),
    blackUsername VARCHAR(255),
    gameobj TEXT(65535) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(whiteUsername) references users(username),
    FOREIGN KEY(blackUsername) references users(username)
);