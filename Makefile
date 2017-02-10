# ves

DATABASE?=ves

init:
	- createuser -s postgres -h localhost
	- createdb -Upostgres -h localhost $(DATABASE)

drop:
	- dropdb -Upostgres -h localhost $(DATABASE)

migrate:
	psql -Upostgres -h localhost $(DATABASE) < ./scripts/base-schema.sql

rebuild: drop init migrate

test:
	lein test

.PHONY: init drop migrate rebuild test
