CREATE TABLE Image(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name varchar(100) NOT NULL,
    location varchar NOT NULL,
    image_type varchar NOT NULL,
    created_at timestamp
);

CREATE TABLE Person(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username varchar(30) NOT NULL,
    password varchar NOT NULL,
    email varchar NOT NULL UNIQUE,
    year_of_birth int NOT NULL,
    address varchar NOT NULL,
    role varchar(30) NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Author(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    full_name varchar(50) NOT NULL UNIQUE,
    description varchar NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Genre(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name varchar(30) NOT NULL UNIQUE,
    description varchar NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Language(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name varchar(30) NOT NULL UNIQUE,
    description varchar NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Publishing_house(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name varchar(50) NOT NULL UNIQUE,
    description varchar NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Storage(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    location varchar NOT NULL UNIQUE,
    description varchar NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Translator(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    full_name varchar(50) NOT NULL UNIQUE,
    description varchar NOT NULL,
    created_at timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Book(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    title varchar(100) NOT NULL,
    author_id bigint NOT NULL REFERENCES Author(id),
    genre_id bigint NOT NULL REFERENCES Genre(id),
    language_id bigint NOT NULL REFERENCES Language(id),
    cover varchar NOT NULL,
    year_of_publishing int NOT NULL,
    publishing_house_id bigint NOT NULL REFERENCES Publishing_house(id),
    translator_id bigint REFERENCES Translator(id),
    isbn varchar NOT NULL UNIQUE,
    number_of_pages int NOT NULL,
    length int NOT NULL,
    width int NOT NULL,
    height int NOT NULL,
    weight int NOT NULL,
    age_limit varchar NOT NULL,
    description varchar NOT NULL,
    created_at timestamp,
    storage_id bigint NOT NULL REFERENCES Storage(id),
    image_id bigint NOT NULL REFERENCES Image(id)
);

CREATE TABLE Rental(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    person_id bigint NOT NULL REFERENCES Person(id),
    book_id bigint NOT NULL REFERENCES Book(id),
    created_at timestamp,
    done timestamp,
    status varchar NOT NULL
);

CREATE TABLE Cart(
    id bigint PRIMARY KEY REFERENCES Person(id)
);

CREATE TABLE Cart_Book(
    id bigint REFERENCES Cart(id),
    book_id bigint REFERENCES Book(id),
    PRIMARY KEY (id, book_id)
);

CREATE TABLE Work(
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    work_type varchar NOT NULL,
    work_kind varchar NOT NULL,
    person_id bigint NOT NULL REFERENCES Person(id),
    author_id bigint REFERENCES Author(id),
    book_id bigint REFERENCES Book(id),
    genre_id bigint REFERENCES Genre(id),
    language_id bigint REFERENCES Language(id),
    publishing_house_id bigint REFERENCES Publishing_house(id),
    rental_id bigint REFERENCES Rental(id),
    storage_id bigint REFERENCES Storage(id),
    translator_id bigint REFERENCES Translator(id),
    person_updated_id bigint REFERENCES Person(id),
    taken_to_work timestamp,
    done timestamp,
    image_id bigint NOT NULL REFERENCES Image(id)
);