CREATE SEQUENCE id_seq START 1000;

CREATE TABLE vertex_types (
    id          bigint DEFAULT nextval('id_seq'::regclass) PRIMARY KEY,
    name        varchar(64),
    created_at  timestamp NOT NULL,
    updated_at  timestamp NOT NULL,
    deleted_at  timestamp
);

CREATE TABLE edge_types (
    id          bigint DEFAULT nextval('id_seq'::regclass) PRIMARY KEY,
    name        varchar(64),
    created_at  timestamp NOT NULL,
    updated_at  timestamp NOT NULL,
    deleted_at  timestamp
);

CREATE TABLE vertices (
    id          bigint DEFAULT nextval('id_seq'::regclass) PRIMARY KEY,
    type        bigint NOT NULL REFERENCES vertex_types(id),
    data        jsonb NOT NULL,
    created_at  timestamp NOT NULL,
    updated_at  timestamp NOT NULL,
    deleted_at  timestamp
);

CREATE TABLE edges (
    id1         bigint NOT NULL,
    type        bigint NOT NULL REFERENCES edge_types(id),
    id2         bigint NOT NULL,
    data        jsonb NOT NULL,
    created_at  timestamp NOT NULL,
    updated_at  timestamp NOT NULL,
    deleted_at  timestamp,
    PRIMARY KEY (id1, type, id2)
);
