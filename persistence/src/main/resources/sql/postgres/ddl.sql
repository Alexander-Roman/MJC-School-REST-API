DROP TABLE certificate_tag;
DROP TABLE tag;
DROP TABLE purchase_item;
DROP TABLE certificate;
DROP TABLE item;
DROP TABLE purchase;
DROP TABLE account;

CREATE TABLE certificate
(
    id               BIGSERIAL     NOT NULL PRIMARY KEY,
    name             VARCHAR(150)  NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    price            DECIMAL(5, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      TIMESTAMP     NOT NULL,
    last_update_date TIMESTAMP     NOT NULL,
    deleted          BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE tag
(
    id   BIGSERIAL   NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    UNIQUE (name)
);

CREATE UNIQUE INDEX tag_name_case_insensitive_unique_index ON tag (LOWER(name));

CREATE TABLE certificate_tag
(
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    certificate_id BIGINT    NOT NULL REFERENCES certificate (id),
    tag_id         BIGINT    NOT NULL REFERENCES tag (id),
    UNIQUE (certificate_id, tag_id)
);

CREATE TABLE account
(
    id       BIGSERIAL    NOT NULL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    blocked  BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX account_email_case_insensitive_unique_index ON account (LOWER(email));

CREATE TABLE purchase
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    cost       DECIMAL   NOT NULL,
    date       TIMESTAMP NOT NULL,
    account_id BIGINT    NOT NULL REFERENCES account (id)
);

CREATE TABLE item
(
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    certificate_id BIGINT    NOT NULL REFERENCES certificate (id),
    count          INTEGER   NOT NULL
);

CREATE TABLE purchase_item
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    purchase_id BIGINT    NOT NULL REFERENCES purchase (id),
    item_id     BIGINT    NOT NULL REFERENCES item (id),
    UNIQUE (purchase_id, item_id)
);
