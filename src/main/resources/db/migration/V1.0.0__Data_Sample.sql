create table circle
(
    id bigint generated by default as identity
        constraint circle_pkey
            primary key,
    circleid varchar(255),
    created_at timestamp,
    enabled boolean default true,
    isdefault boolean default false,
    csv boolean default false,
    match jsonb,
    matchcsv jsonb,
    name varchar(255),
    parameters jsonb,
    parameters_size integer,
    updated_at timestamp,
    workspaceid varchar(255)
);

alter table circle owner to circlematcher;


CREATE INDEX circle_matchcsv_gin_idx ON circle
    USING gin ((matchcsv) jsonb_path_ops);

CREATE INDEX circle_parameters_gin_idx ON circle
    USING gin ((parameters) jsonb_path_ops);


