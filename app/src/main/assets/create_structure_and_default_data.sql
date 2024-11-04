CREATE TABLE StoreItem (
	id	TEXT,
	name	TEXT,
	type	TEXT,
	thumbnail	TEXT,
	selected_thumbnail	TEXT,
	url	TEXT,
	signature	TEXT,
	last_modified	INTEGER,
	status	TEXT
);
CREATE TABLE ItemPackage (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	name	TEXT NOT NULL,
	thumbnail	TEXT,
	selected_thumbnail	TEXT,
	type	TEXT NOT NULL,
	folder	TEXT,
	id_str	TEXT,
	last_modified	TEXT,
	status	TEXT
);
CREATE TABLE ImageTemplate (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	name	TEXT,
	thumbnail	TEXT,
	selected_thumbnail	TEXT,
	preview	TEXT,
	template	TEXT,
	child	TEXT,
	package_id	NUMERIC,
	last_modified	TEXT,
	status	TEXT
);