# AutonomousSystemThesis
Master Thesis Project

Supervisor: Donald Degraen, German Research Center for Artificial Intelligence, Saarbrücken, Germany


// TODO: INSTALLATIONS: maybe delete it later
// Notes for me: PostgreSQL 11:
// directory under which you store data: /Library/PostgreSQL/11/data
// Psw for database superuser(postgres): root
// Port number the server should listen on: 5433
// http://127.0.0.1:56084/browser/

/* Created the test Table on PostgreSQL

CREATE TABLE public."testTableThesis"
(
    id integer NOT NULL DEFAULT nextval('"testTableThesis_id_seq"'::regclass) 
    ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    CONSTRAINT id PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."testTableThesis"
    OWNER to postgres;

----------------------------------------------------------------------------------------------------
ALTER TABLE public."testTableThesis"
    ADD COLUMN id integer NOT NULL DEFAULT nextval('"testTableThesis_id_seq"'::regclass)  
    ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 )
----------------------------------------------------------------------------------------------------
ALTER TABLE public."testTableThesis"
    ADD COLUMN "timestamp" timestamp with time zone NOT NULL
----------------------------------------------------------------------------------------------------
ALTER TABLE public."testTableThesis"
    ADD COLUMN "app_pinkBeacon_distance" integer
----------------------------------------------------------------------------------------------------
ALTER TABLE public."testTableThesis"
    ADD COLUMN "app_yellowBeacon_distance" integer
----------------------------------------------------------------------------------------------------
*/