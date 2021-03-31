DROP TABLE certificate_tag;
DROP TABLE certificate;
DROP TABLE tag;

CREATE TABLE certificate
(
    certificate_id   BIGSERIAL    NOT NULL PRIMARY KEY,
    certificate_name VARCHAR(150) NOT NULL,
    description      VARCHAR(255),
    price            DECIMAL(5, 2),
    duration         INT,
    create_date      TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);

create table tag
(
    tag_id   BIGSERIAL NOT NULL PRIMARY KEY,
    tag_name VARCHAR(50),
    UNIQUE (tag_name)
);

CREATE UNIQUE INDEX tag_name_case_insensitive_unique_index ON tag (LOWER(tag_name));

create table certificate_tag
(
    certificate_tag_id BIGSERIAL,
    certificate_id     BIGINT NOT NULL REFERENCES certificate (certificate_id),
    tag_id             BIGINT NOT NULL REFERENCES tag (tag_id),
    UNIQUE (certificate_id, tag_id)
);
