CREATE TABLE restapikeys.apikeys (
	id INTEGER auto_increment NOT NULL,
	user_name varchar(100) NOT NULL,	
	salt varchar(50) NOT NULL,
	hashed_uuid varchar(150) NOT NULL,
	api_scope varchar(100) NOT NULL,
	active BOOL DEFAULT true NOT NULL,
	CONSTRAINT apikeys_PK PRIMARY KEY (id),
	CONSTRAINT apikeys_UN UNIQUE KEY (user_name)
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;