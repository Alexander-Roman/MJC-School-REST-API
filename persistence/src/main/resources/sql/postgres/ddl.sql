DROP TABLE certificate_tag;
DROP TABLE certificate;
DROP TABLE tag;

CREATE TABLE certificate
(
    id               BIGSERIAL     NOT NULL PRIMARY KEY,
    name             VARCHAR(150)  NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    price            DECIMAL(5, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      TIMESTAMP     NOT NULL,
    last_update_date TIMESTAMP     NOT NULL
);

create table tag
(
    id   BIGSERIAL   NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    UNIQUE (name)
);

CREATE UNIQUE INDEX tag_name_case_insensitive_unique_index ON tag (LOWER(name));

create table certificate_tag
(
    id             BIGSERIAL,
    certificate_id BIGINT NOT NULL REFERENCES certificate (id),
    tag_id         BIGINT NOT NULL REFERENCES tag (id),
    UNIQUE (certificate_id, tag_id)
);
