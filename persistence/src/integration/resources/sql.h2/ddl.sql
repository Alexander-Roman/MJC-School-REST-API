CREATE SEQUENCE certificate_id_seq;

CREATE TABLE certificate
(
    id               BIGINT                 DEFAULT certificate_id_seq.nextval,
    name             VARCHAR(150)  NOT NULL,
    description      VARCHAR(255)  NOT NULL,
    price            DECIMAL(5, 2) NOT NULL,
    duration         INT           NOT NULL,
    create_date      DATETIME      NOT NULL,
    last_update_date DATETIME      NOT NULL,
    deleted          BOOLEAN       NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE SEQUENCE tag_id_seq;

CREATE TABLE tag
(
    id   BIGINT DEFAULT tag_id_seq.nextval,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX tag_name_unique_index ON tag (name);

CREATE TABLE certificate_tag
(
    id             BIGINT AUTO_INCREMENT,
    certificate_id BIGINT NOT NULL,
    tag_id         BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (certificate_id) REFERENCES certificate (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id),
    CONSTRAINT certificate_tag_pair_constraint UNIQUE (certificate_id, tag_id)
);
