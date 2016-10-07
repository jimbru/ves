# ves-lib

DATABASE?=ves

init:
	- createuser -s postgres -h localhost
	- createdb -Upostgres -h localhost $(DATABASE)

drop:
	- dropdb -Upostgres -h localhost $(DATABASE)

migrate:
	cd ../ves-tool && lein run -- init | psql $(DATABASE)
	cd ../ves-tool && lein run -- create-vertex FOO | psql $(DATABASE)
	cd ../ves-tool && lein run -- create-vertex BAR | psql $(DATABASE)
	cd ../ves-tool && lein run -- create-edge RED | psql $(DATABASE)
	cd ../ves-tool && lein run -- create-vertex BLUE | psql $(DATABASE)

rebuild: drop init migrate

test:
	lein test

.PHONY: init drop migrate rebuild test
